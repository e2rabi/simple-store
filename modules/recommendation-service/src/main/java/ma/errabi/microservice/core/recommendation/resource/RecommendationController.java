package ma.errabi.microservice.core.recommendation.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.recommendation.service.RecommendationService;
import ma.errabi.sdk.api.common.CustomPage;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.recommendation.RecommendationResource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendationController implements RecommendationResource {
    private final RecommendationService recommendationService;

    @Override
    public RecommendationDTO createRecommendation(RecommendationDTO dto) {
        log.debug("createRecommendation: tries to create recommendations for the product with productId: {}", dto.getProductId());
       return recommendationService.createRecommendation(dto);
    }
    @Override
    @DeleteMapping(value = "/recommendation/product/{productId}")
    public void deleteRecommendations(@PathVariable String productId) {
        log.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
        recommendationService.deleteRecommendations(productId);
    }
    @Override
    @GetMapping(value = "/recommendation/product/{productId}",produces = "application/json")
    public CustomPage<RecommendationDTO> getRecommendationByProductId(@PathVariable String productId, Pageable page) {
        log.debug("scanRecommendationByAuthor: tries to get recommendations by author");
       return recommendationService.scanByRecommendationByProductId(productId,page);
    }
}
