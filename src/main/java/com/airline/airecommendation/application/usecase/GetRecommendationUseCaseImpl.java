package com.airline.airecommendation.application.usecase;

import com.airline.airecommendation.domain.model.Recommendation;
import com.airline.airecommendation.domain.model.RecommendationStatus;
import com.airline.airecommendation.domain.port.in.GetRecommendationUseCase;
import com.airline.airecommendation.domain.port.out.RecommendationRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GetRecommendationUseCaseImpl implements GetRecommendationUseCase {

    private final RecommendationRepositoryPort recommendationRepository;

    public GetRecommendationUseCaseImpl(RecommendationRepositoryPort recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Optional<Recommendation> findById(UUID id) {
        return recommendationRepository.findById(id);
    }

    @Override
    public List<Recommendation> findAll() {
        return recommendationRepository.findAll();
    }

    @Override
    public List<Recommendation> findByStatus(RecommendationStatus status) {
        return recommendationRepository.findByStatus(status);
    }

    @Override
    public List<Recommendation> findByImpactReportId(UUID impactReportId) {
        return recommendationRepository.findByImpactReportId(impactReportId);
    }
}
