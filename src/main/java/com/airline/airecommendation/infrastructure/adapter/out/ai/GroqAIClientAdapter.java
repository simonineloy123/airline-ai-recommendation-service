package com.airline.airecommendation.infrastructure.adapter.out.ai;

import com.airline.airecommendation.domain.model.*;
import com.airline.airecommendation.domain.port.out.AIClientPort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@ApplicationScoped
public class GroqAIClientAdapter implements AIClientPort {

    private static final Logger LOG = Logger.getLogger(GroqAIClientAdapter.class);

    @ConfigProperty(name = "groq.api.key")
    String apiKey;

    @ConfigProperty(name = "groq.api.url")
    String apiUrl;

    @ConfigProperty(name = "groq.model")
    String model;

    @ConfigProperty(name = "groq.max.tokens")
    int maxTokens;

    @ConfigProperty(name = "groq.temperature")
    double temperature;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient   httpClient   = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    @Override
    public Recommendation generateRecommendation(ImpactEventPayload impact) {
        try {
            String prompt = buildPrompt(impact);
            String response = callGroqAPI(prompt);
            return parseResponse(response, impact);
        } catch (Exception e) {
            LOG.errorf("Error llamando a Groq API: %s — usando fallback", e.getMessage());
            return buildFallbackRecommendation(impact);
        }
    }

    private String buildPrompt(ImpactEventPayload impact) {
        return String.format("""
            Eres un experto en operaciones de aerolíneas. Analiza esta disrupción y genera UNA recomendación operativa.
            
            DISRUPCIÓN:
            - Vuelo: %s (%s → %s)
            - Tipo: %s
            - Pasajeros afectados: %d
            - Vuelos afectados: %d
            - Retraso total: %d minutos
            - Severidad: %s
            
            Responde SOLO en este formato JSON, sin texto adicional:
            {
              "type": "TIPO_RECOMENDACION",
              "description": "descripción corta y clara de la acción",
              "reasoning": "explicación del por qué esta es la mejor acción",
              "confidenceScore": 0.95
            }
            
            Tipos válidos: DELAY_FLIGHT, CANCEL_FLIGHT, REBOOK_PASSENGERS, CHANGE_AIRCRAFT, NOTIFY_PASSENGERS, OFFER_COMPENSATION, DIVERT_FLIGHT, EXPEDITE_TURNAROUND
            """,
            impact.getFlightNumber(),
            impact.getOrigin(),
            impact.getDestination(),
            impact.getDisruptionType(),
            impact.getAffectedPassengers(),
            impact.getAffectedFlights(),
            impact.getTotalDelayMinutes(),
            impact.getSeverity()
        );
    }

    private String callGroqAPI(String prompt) throws Exception {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", model);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", temperature);

        ArrayNode messages = objectMapper.createArrayNode();
        ObjectNode message = objectMapper.createObjectNode();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.set("messages", messages);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(
                objectMapper.writeValueAsString(requestBody)))
            .timeout(Duration.ofSeconds(30))
            .build();

        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Groq API error: " + response.statusCode() + " " + response.body());
        }

        JsonNode responseJson = objectMapper.readTree(response.body());
        return responseJson
            .path("choices").get(0)
            .path("message")
            .path("content")
            .asText();
    }

    private Recommendation parseResponse(String content, ImpactEventPayload impact) {
        try {
            String cleanContent = content.trim();
            if (cleanContent.contains("```json")) {
                cleanContent = cleanContent.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");
            }

            JsonNode json = objectMapper.readTree(cleanContent);

            RecommendationType type = RecommendationType.valueOf(
                json.path("type").asText("NOTIFY_PASSENGERS"));

            return Recommendation.builder()
                .impactReportId(impact.getId())
                .flightNumber(impact.getFlightNumber())
                .origin(impact.getOrigin())
                .destination(impact.getDestination())
                .type(type)
                .description(json.path("description").asText())
                .reasoning(json.path("reasoning").asText())
                .confidenceScore(json.path("confidenceScore").asDouble(0.8))
                .affectedPassengers(impact.getAffectedPassengers())
                .build();

        } catch (Exception e) {
            LOG.errorf("Error parseando respuesta de Groq: %s", e.getMessage());
            return buildFallbackRecommendation(impact);
        }
    }

    private Recommendation buildFallbackRecommendation(ImpactEventPayload impact) {
        RecommendationType type = switch (impact.getDisruptionType()) {
            case "CANCELLATION" -> RecommendationType.REBOOK_PASSENGERS;
            case "DELAY"        -> impact.getTotalDelayMinutes() > 120
                ? RecommendationType.OFFER_COMPENSATION
                : RecommendationType.NOTIFY_PASSENGERS;
            default             -> RecommendationType.NOTIFY_PASSENGERS;
        };

        return Recommendation.builder()
            .impactReportId(impact.getId())
            .flightNumber(impact.getFlightNumber())
            .origin(impact.getOrigin())
            .destination(impact.getDestination())
            .type(type)
            .description("Acción recomendada basada en análisis de impacto")
            .reasoning("Recomendación generada por heurísticas locales")
            .confidenceScore(0.75)
            .affectedPassengers(impact.getAffectedPassengers())
            .build();
    }
}
