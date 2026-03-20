package com.airline.airecommendation.domain.port.out;

import com.airline.airecommendation.domain.model.ImpactEventPayload;
import com.airline.airecommendation.domain.model.Recommendation;

public interface AIClientPort {
    Recommendation generateRecommendation(ImpactEventPayload impact);
}
