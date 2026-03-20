package com.airline.airecommendation.shared.mapper;

import com.airline.airecommendation.domain.model.Recommendation;
import com.airline.airecommendation.infrastructure.adapter.out.persistence.RecommendationEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RecommendationMapper {

    public Recommendation toDomain(RecommendationEntity entity) {
        return Recommendation.builder()
            .id(entity.id)
            .impactReportId(entity.impactReportId)
            .flightNumber(entity.flightNumber)
            .origin(entity.origin)
            .destination(entity.destination)
            .type(entity.type)
            .status(entity.status)
            .description(entity.description)
            .reasoning(entity.reasoning)
            .confidenceScore(entity.confidenceScore)
            .affectedPassengers(entity.affectedPassengers)
            .generatedAt(entity.generatedAt)
            .appliedAt(entity.appliedAt)
            .build();
    }

    public RecommendationEntity toEntity(Recommendation domain) {
        RecommendationEntity entity  = new RecommendationEntity();
        entity.impactReportId        = domain.getImpactReportId();
        entity.flightNumber          = domain.getFlightNumber();
        entity.origin                = domain.getOrigin();
        entity.destination           = domain.getDestination();
        entity.type                  = domain.getType();
        entity.status                = domain.getStatus();
        entity.description           = domain.getDescription();
        entity.reasoning             = domain.getReasoning();
        entity.confidenceScore       = domain.getConfidenceScore();
        entity.affectedPassengers    = domain.getAffectedPassengers();
        entity.generatedAt           = domain.getGeneratedAt();
        entity.appliedAt             = domain.getAppliedAt();
        return entity;
    }
}
