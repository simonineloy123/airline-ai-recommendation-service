package com.airline.airecommendation.domain.port.in;

import com.airline.airecommendation.domain.model.Recommendation;
import com.airline.airecommendation.domain.model.RecommendationStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetRecommendationUseCase {
    Optional<Recommendation> findById(UUID id);
    List<Recommendation> findAll();
    List<Recommendation> findByStatus(RecommendationStatus status);
    List<Recommendation> findByImpactReportId(UUID impactReportId);
}
