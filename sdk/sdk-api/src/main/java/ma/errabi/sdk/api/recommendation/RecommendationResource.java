package ma.errabi.sdk.api.recommendation;

import ma.errabi.sdk.api.common.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;



public interface RecommendationResource {

    @PostMapping(value = "/recommendation",consumes = "application/json")
    RecommendationDTO createRecommendation(@RequestBody RecommendationDTO dto);
    void deleteRecommendations(@PathVariable String productId);
    @GetMapping(value = "/recommendation/product/{productId}",produces = "application/json")
    CustomPage<RecommendationDTO> getRecommendationByProductId(@PathVariable String productId, Pageable page);
}
