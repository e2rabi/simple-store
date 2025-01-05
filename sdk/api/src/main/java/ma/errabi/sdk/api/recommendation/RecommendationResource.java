package ma.errabi.sdk.api.recommendation;

import ma.errabi.sdk.api.common.CustomPage;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface RecommendationResource {
    @GetMapping(value = "/recommendation/{id}/product/{productId}",produces = "application/json")
    RecommendationDTO getRecommendations(@PathVariable String id,@PathVariable String productId);
    @PostMapping(value = "/recommendation",consumes = "application/json")
    RecommendationDTO createRecommendation(@RequestBody RecommendationDTO dto);
    @DeleteMapping(value = "/recommendation/{id}")
    void deleteRecommendations(@PathVariable String id);
    @GetMapping(value = "/recommendation",produces = "application/json")
    List<RecommendationDTO> getAllRecommendations();
    @GetMapping(value = "/recommendation/rating",produces = "application/json")
    CustomPage<RecommendationDTO> getRecommendationByRating(@RequestParam Integer minRating, @RequestParam Integer maxRating);
}
