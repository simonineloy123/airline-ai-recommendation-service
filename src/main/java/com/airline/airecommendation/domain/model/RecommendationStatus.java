package com.airline.airecommendation.domain.model;

public enum RecommendationStatus {
    PENDING,    // generada, esperando acción
    APPLIED,    // aplicada por el operador
    REJECTED,   // rechazada por el operador
    EXPIRED;    // expiró sin acción
}
