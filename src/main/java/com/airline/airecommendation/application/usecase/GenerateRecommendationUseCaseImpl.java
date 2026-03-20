package com.airline.airecommendation.application.usecase;

import com.airline.airecommendation.domain.model.ImpactEventPayload;
import com.airline.airecommendation.domain.model.Recommendation;
import com.airline.airecommendation.domain.port.in.GenerateRecommendationUseCase;
import com.airline.airecommendation.domain.port.out.AIClientPort;
import com.airline.airecommendation.domain.port.out.RecommendationEventPublisherPort;
import com.airline.airecommendation.domain.port.out.RecommendationRepositoryPort;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class GenerateRecommendationUseCaseImpl implements GenerateRecommendationUseCase {

    private static final Logger LOG = Logger.getLogger(GenerateRecommendationUseCaseImpl.class);

    private final AIClientPort                  aiClient;
    private final RecommendationRepositoryPort  recommendationRepository;
    private final RecommendationEventPublisherPort eventPublisher;
    private final Event<Recommendation>         recommendationEvent;
    private final Vertx                         vertx;

    public GenerateRecommendationUseCaseImpl(
        AIClientPort aiClient,
        RecommendationRepositoryPort recommendationRepository,
        RecommendationEventPublisherPort eventPublisher,
        Event<Recommendation> recommendationEvent,
        Vertx vertx
    ) {
        this.aiClient               = aiClient;
        this.recommendationRepository = recommendationRepository;
        this.eventPublisher         = eventPublisher;
        this.recommendationEvent    = recommendationEvent;
        this.vertx                  = vertx;
    }

    @Override
    public Recommendation execute(ImpactEventPayload impact) {
        LOG.debugf("Generando recomendación para vuelo: %s | severidad: %s",
            impact.getFlightNumber(), impact.getSeverity());

        Recommendation recommendation = aiClient.generateRecommendation(impact);

        vertx.executeBlocking(() -> {
            saveAndPublish(recommendation);
            return null;
        });

        return recommendation;
    }

    @Transactional
    public void saveAndPublish(Recommendation recommendation) {
        Recommendation saved = recommendationRepository.save(recommendation);
        LOG.infof("Recomendación generada → vuelo: %s | tipo: %s | confianza: %.2f",
            saved.getFlightNumber(),
            saved.getType(),
            saved.getConfidenceScore());
        recommendationEvent.fire(saved);
    }

    public void onRecommendationSaved(
        @Observes(during = TransactionPhase.AFTER_SUCCESS) Recommendation recommendation
    ) {
        eventPublisher.publish(recommendation);
    }
}
