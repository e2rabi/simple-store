package ma.errabi.sdk.api.recommendation;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface RecommendationResource {
    @GetMapping(value = "/recommendation/{productId}",produces = "application/json")
    List<RecommendationDTO> getRecommendations(String productId);
}
