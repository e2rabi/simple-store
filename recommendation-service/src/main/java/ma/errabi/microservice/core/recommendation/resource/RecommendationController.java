package ma.errabi.microservice.core.recommendation.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.recommendation.service.RecommendationService;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.recommendation.RecommendationResource;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendationController implements RecommendationResource {
    private final RecommendationService recommendationService;

    @Override
    public Flux<RecommendationDTO> getRecommendations(String productId) {
        log.debug("getRecommendations: tries to get recommendations for the product with productId: {}", productId);
       return recommendationService.getRecommendations(productId);
    }

    @Override
    public Mono<RecommendationDTO> createRecommendation(RecommendationDTO dto) {
        log.debug("createRecommendation: tries to create recommendations for the product with productId: {}", dto.getProductId());
       return recommendationService.createRecommendation(dto);
    }

    @Override
    public Mono<Void> deleteRecommendations(String productId) {
        log.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
        return recommendationService.deleteRecommendations(productId);
    }
}
