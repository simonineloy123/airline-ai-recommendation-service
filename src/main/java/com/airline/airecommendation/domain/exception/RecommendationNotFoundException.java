package com.airline.airecommendation.domain.exception;

import java.util.UUID;

public class RecommendationNotFoundException extends RuntimeException {
    public RecommendationNotFoundException(UUID id) {
        super("Recomendación no encontrada con id: " + id);
    }
}
