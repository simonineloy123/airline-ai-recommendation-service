package com.airline.airecommendation.domain;

import com.airline.airecommendation.domain.model.*;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class RecommendationTest {

    @Test
    void shouldCreateRecommendationWithDefaults() {
        Recommendation rec = Recommendation.builder()
            .impactReportId(UUID.randomUUID())
            .flightNumber("AR1234")
            .origin("EZE")
            .destination("MAD")
            .type(RecommendationType.NOTIFY_PASSENGERS)
            .description("Notificar pasajeros")
            .reasoning("Retraso mayor a 60 minutos")
            .confidenceScore(0.92)
            .affectedPassengers(200)
            .build();

        assertNotNull(rec.getId());
        assertNotNull(rec.getGeneratedAt());
        assertEquals(RecommendationStatus.PENDING, rec.getStatus());
        assertNull(rec.getAppliedAt());
    }

    @Test
    void shouldSetHighConfidenceScore() {
        Recommendation rec = Recommendation.builder()
            .impactReportId(UUID.randomUUID())
            .flightNumber("LA5678")
            .origin("SCL").destination("GRU")
            .type(RecommendationType.REBOOK_PASSENGERS)
            .description("Reubicar en próximo vuelo")
            .reasoning("Cancellación confirmada")
            .confidenceScore(0.98)
            .affectedPassengers(300)
            .build();

        assertEquals(0.98, rec.getConfidenceScore(), 0.001);
        assertEquals(RecommendationType.REBOOK_PASSENGERS, rec.getType());
    }

    @Test
    void shouldFailWithoutImpactReportId() {
        assertThrows(IllegalArgumentException.class, () ->
            Recommendation.builder()
                .flightNumber("AR0000")
                .type(RecommendationType.NOTIFY_PASSENGERS)
                .build()
        );
    }

    @Test
    void shouldContainAllRecommendationTypes() {
        RecommendationType[] types = RecommendationType.values();
        assertEquals(8, types.length);
    }
}