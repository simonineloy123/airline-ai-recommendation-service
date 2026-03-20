package com.airline.airecommendation.infrastructure.adapter.out.persistence;

import com.airline.airecommendation.domain.model.RecommendationStatus;
import com.airline.airecommendation.domain.model.RecommendationType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recommendations")
public class RecommendationEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    public UUID id;

    @Column(name = "impact_report_id", nullable = false)
    public UUID impactReportId;

    @Column(name = "flight_number", nullable = false, length = 10)
    public String flightNumber;

    @Column(name = "origin", length = 10)
    public String origin;

    @Column(name = "destination", length = 10)
    public String destination;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    public RecommendationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    public RecommendationStatus status;

    @Column(name = "description", length = 500)
    public String description;

    @Column(name = "reasoning", length = 1000)
    public String reasoning;

    @Column(name = "confidence_score", nullable = false)
    public double confidenceScore;

    @Column(name = "affected_passengers", nullable = false)
    public int affectedPassengers;

    @Column(name = "generated_at", nullable = false)
    public LocalDateTime generatedAt;

    @Column(name = "applied_at")
    public LocalDateTime appliedAt;

    @PrePersist
    public void prePersist() {
        if (generatedAt == null) generatedAt = LocalDateTime.now();
        if (status == null) status = RecommendationStatus.PENDING;
    }

    public static List<RecommendationEntity> findByStatus(RecommendationStatus status) {
        return list("status", status);
    }

    public static List<RecommendationEntity> findByImpactReportId(UUID impactReportId) {
        return list("impactReportId", impactReportId);
    }
}
