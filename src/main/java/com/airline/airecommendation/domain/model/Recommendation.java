package com.airline.airecommendation.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Recommendation {

    private UUID                 id;
    private UUID                 impactReportId;
    private String               flightNumber;
    private String               origin;
    private String               destination;
    private RecommendationType   type;
    private RecommendationStatus status;
    private String               description;
    private String               reasoning;
    private double               confidenceScore;
    private int                  affectedPassengers;
    private LocalDateTime        generatedAt;
    private LocalDateTime        appliedAt;

    private Recommendation() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Recommendation r = new Recommendation();

        public Builder id(UUID id)                          { r.id = id; return this; }
        public Builder impactReportId(UUID v)               { r.impactReportId = v; return this; }
        public Builder flightNumber(String v)               { r.flightNumber = v; return this; }
        public Builder origin(String v)                     { r.origin = v; return this; }
        public Builder destination(String v)                { r.destination = v; return this; }
        public Builder type(RecommendationType v)           { r.type = v; return this; }
        public Builder status(RecommendationStatus v)       { r.status = v; return this; }
        public Builder description(String v)                { r.description = v; return this; }
        public Builder reasoning(String v)                  { r.reasoning = v; return this; }
        public Builder confidenceScore(double v)            { r.confidenceScore = v; return this; }
        public Builder affectedPassengers(int v)            { r.affectedPassengers = v; return this; }
        public Builder generatedAt(LocalDateTime v)         { r.generatedAt = v; return this; }
        public Builder appliedAt(LocalDateTime v)           { r.appliedAt = v; return this; }

        public Recommendation build() {
            if (r.id == null)          r.id = UUID.randomUUID();
            if (r.status == null)      r.status = RecommendationStatus.PENDING;
            if (r.generatedAt == null) r.generatedAt = LocalDateTime.now();
            validate();
            return r;
        }

        private void validate() {
            if (r.impactReportId == null)
                throw new IllegalArgumentException("El ID del reporte de impacto es obligatorio");
            if (r.flightNumber == null || r.flightNumber.isBlank())
                throw new IllegalArgumentException("El número de vuelo es obligatorio");
            if (r.type == null)
                throw new IllegalArgumentException("El tipo de recomendación es obligatorio");
        }
    }

    public UUID getId()                       { return id; }
    public UUID getImpactReportId()           { return impactReportId; }
    public String getFlightNumber()           { return flightNumber; }
    public String getOrigin()                 { return origin; }
    public String getDestination()            { return destination; }
    public RecommendationType getType()       { return type; }
    public RecommendationStatus getStatus()   { return status; }
    public String getDescription()            { return description; }
    public String getReasoning()              { return reasoning; }
    public double getConfidenceScore()        { return confidenceScore; }
    public int getAffectedPassengers()        { return affectedPassengers; }
    public LocalDateTime getGeneratedAt()     { return generatedAt; }
    public LocalDateTime getAppliedAt()       { return appliedAt; }
}
