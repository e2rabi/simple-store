package ma.errabi.sdk.api.recommendation;

import ma.errabi.sdk.api.common.CustomPage;
import org.springframework.web.bind.annotation.*;

public interface RecommendationResource {

    @PostMapping(value = "/recommendation",consumes = "application/json")
    RecommendationDTO createRecommendation(@RequestBody RecommendationDTO dto);
    @DeleteMapping(value = "/recommendation/{id}")
    void deleteRecommendations(@PathVariable String id);
    @GetMapping(value = "/recommendation/product/{productId}",produces = "application/json")
    CustomPage<RecommendationDTO> getRecommendationByProductId(@PathVariable String productId);
}
