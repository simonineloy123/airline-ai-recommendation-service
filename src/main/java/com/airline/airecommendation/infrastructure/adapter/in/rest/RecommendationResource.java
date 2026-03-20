package com.airline.airecommendation.infrastructure.adapter.in.rest;

import com.airline.airecommendation.domain.model.Recommendation;
import com.airline.airecommendation.domain.model.RecommendationStatus;
import com.airline.airecommendation.domain.port.in.GetRecommendationUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/recommendations")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecommendationResource {

    private final GetRecommendationUseCase getRecommendationUseCase;

    public RecommendationResource(GetRecommendationUseCase getRecommendationUseCase) {
        this.getRecommendationUseCase = getRecommendationUseCase;
    }

    @GET
    public List<Recommendation> getAll() {
        return getRecommendationUseCase.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        return getRecommendationUseCase.findById(id)
            .map(r -> Response.ok(r).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/status/{status}")
    public List<Recommendation> getByStatus(@PathParam("status") String status) {
        return getRecommendationUseCase.findByStatus(RecommendationStatus.valueOf(status));
    }
}
