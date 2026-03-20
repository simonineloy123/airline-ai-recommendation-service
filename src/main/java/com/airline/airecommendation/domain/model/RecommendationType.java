package com.airline.airecommendation.domain.model;

public enum RecommendationType {
    DELAY_FLIGHT,           // Retrasar vuelo estratégicamente
    CANCEL_FLIGHT,          // Cancelar vuelo
    REBOOK_PASSENGERS,      // Reubicar pasajeros en otros vuelos
    CHANGE_AIRCRAFT,        // Cambiar aeronave
    NOTIFY_PASSENGERS,      // Notificar pasajeros proactivamente
    OFFER_COMPENSATION,     // Ofrecer compensación
    DIVERT_FLIGHT,          // Desviar vuelo
    EXPEDITE_TURNAROUND;    // Acelerar rotación de aeronave
}
