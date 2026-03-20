package com.airline.airecommendation.domain.port.out;

import com.airline.airecommendation.domain.model.Recommendation;

public interface RecommendationEventPublisherPort {
    void publish(Recommendation recommendation);
}
