package com.airline.airecommendation.domain.port.in;

import com.airline.airecommendation.domain.model.ImpactEventPayload;
import com.airline.airecommendation.domain.model.Recommendation;

public interface GenerateRecommendationUseCase {
    Recommendation execute(ImpactEventPayload impact);
}
