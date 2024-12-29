package ma.errabi.sdk.api.recommendation;

import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface RecommendationResource {
    @GetMapping(value = "/recommendation/{productId}",produces = "application/json")
    RecommendationDTO getRecommendations(@PathVariable String productId);
    @PostMapping(value = "/recommendation",consumes = "application/json")
    RecommendationDTO createRecommendation(@RequestBody RecommendationDTO dto);
    @DeleteMapping(value = "/recommendation/{productId}")
    void deleteRecommendations(@PathVariable String productId);
    @GetMapping(value = "/recommendation",produces = "application/json")
    List<RecommendationDTO> getAllRecommendations();
}
