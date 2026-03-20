package com.airline.airecommendation.infrastructure.adapter.out.persistence;

import com.airline.airecommendation.domain.model.Recommendation;
import com.airline.airecommendation.domain.model.RecommendationStatus;
import com.airline.airecommendation.domain.port.out.RecommendationRepositoryPort;
import com.airline.airecommendation.shared.mapper.RecommendationMapper;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RecommendationRepositoryAdapter implements RecommendationRepositoryPort {

    private final RecommendationMapper mapper;

    public RecommendationRepositoryAdapter(RecommendationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Recommendation save(Recommendation recommendation) {
        RecommendationEntity entity = mapper.toEntity(recommendation);
        RecommendationEntity.persist(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Recommendation> findById(UUID id) {
        return RecommendationEntity.<RecommendationEntity>findByIdOptional(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<Recommendation> findAll() {
        return RecommendationEntity.<RecommendationEntity>listAll()
            .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Recommendation> findByStatus(RecommendationStatus status) {
        return RecommendationEntity.findByStatus(status)
            .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Recommendation> findByImpactReportId(UUID impactReportId) {
        return RecommendationEntity.findByImpactReportId(impactReportId)
            .stream().map(mapper::toDomain).toList();
    }
}
