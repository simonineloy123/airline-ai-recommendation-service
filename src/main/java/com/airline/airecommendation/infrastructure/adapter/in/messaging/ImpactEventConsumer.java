package com.airline.airecommendation.infrastructure.adapter.in.messaging;

import com.airline.airecommendation.domain.model.ImpactEventPayload;
import com.airline.airecommendation.domain.port.in.GenerateRecommendationUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ImpactEventConsumer {

    private static final Logger LOG = Logger.getLogger(ImpactEventConsumer.class);

    private final GenerateRecommendationUseCase generateRecommendationUseCase;
    private final ObjectMapper                  objectMapper;

    public ImpactEventConsumer(GenerateRecommendationUseCase generateRecommendationUseCase) {
        this.generateRecommendationUseCase = generateRecommendationUseCase;
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    }

    @Incoming("impact-events-in")
    public void consume(String message) {
        try {
            LOG.debugf("Evento de impacto recibido: %s", message);
            ImpactEventPayload payload = objectMapper.readValue(message, ImpactEventPayload.class);
            generateRecommendationUseCase.execute(payload);
        } catch (Exception e) {
            LOG.errorf("Error procesando evento de impacto: %s | error: %s",
                message, e.getMessage());
        }
    }
}
