package com.airline.airecommendation.infrastructure.adapter.out.messaging;

import com.airline.airecommendation.domain.model.Recommendation;
import com.airline.airecommendation.domain.port.out.RecommendationEventPublisherPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RecommendationEventPublisherAdapter implements RecommendationEventPublisherPort {

    private static final Logger LOG = Logger.getLogger(RecommendationEventPublisherAdapter.class);

    private final Emitter<String> recommendationEventsEmitter;
    private final ObjectMapper    objectMapper;

    public RecommendationEventPublisherAdapter(
        @Channel("recommendation-events") Emitter<String> recommendationEventsEmitter
    ) {
        this.recommendationEventsEmitter = recommendationEventsEmitter;
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    }

    @Override
    public void publish(Recommendation recommendation) {
        try {
            String payload = buildPayload(recommendation);
            recommendationEventsEmitter.send(payload);
            LOG.infof("Recomendación publicada → topic: recommendation_events | vuelo: %s | tipo: %s | confianza: %.2f",
                recommendation.getFlightNumber(),
                recommendation.getType(),
                recommendation.getConfidenceScore());
        } catch (JsonProcessingException e) {
            LOG.errorf("Error serializando recomendación: %s", e.getMessage());
            throw new RuntimeException("Error publicando recomendación a Kafka", e);
        }
    }

    private String buildPayload(Recommendation recommendation) throws JsonProcessingException {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("id",                 recommendation.getId().toString());
        node.put("impactReportId",     recommendation.getImpactReportId() != null ? recommendation.getImpactReportId().toString() : null);
        node.put("flightNumber",       recommendation.getFlightNumber());
        node.put("origin",             recommendation.getOrigin());
        node.put("destination",        recommendation.getDestination());
        node.put("type",               recommendation.getType().name());
        node.put("status",             recommendation.getStatus().name());
        node.put("description",        recommendation.getDescription());
        node.put("reasoning",          recommendation.getReasoning());
        node.put("confidenceScore",    recommendation.getConfidenceScore());
        node.put("affectedPassengers", recommendation.getAffectedPassengers());
        node.put("generatedAt",        recommendation.getGeneratedAt().toString());
        return objectMapper.writeValueAsString(node);
    }
}
