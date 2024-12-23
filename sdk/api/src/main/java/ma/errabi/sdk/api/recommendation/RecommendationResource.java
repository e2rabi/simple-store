package ma.errabi.sdk.api.recommendation;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecommendationResource {
    @GetMapping(value = "/recommendation/{productId}",produces = "application/json")
    Flux<RecommendationDTO> getRecommendations(@PathVariable String productId);
    @PostMapping(value = "/recommendation",consumes = "application/json")
    Mono<RecommendationDTO> createRecommendation(@RequestBody RecommendationDTO dto);
    @DeleteMapping(value = "/recommendation/{productId}")
    Mono<Void> deleteRecommendations(@PathVariable String productId);
}
