package ma.errabi.microservice.core.recommendation.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.recommendation.service.RecommendationService;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.recommendation.RecommendationResource;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendationController implements RecommendationResource {
    private final RecommendationService recommendationService;

    @Override
    public RecommendationDTO getRecommendations(String id) {
        log.debug("getRecommendations: tries to get recommendations for the product with productId: {}", id);
       return recommendationService.getRecommendations(id);
    }

    @Override
    public RecommendationDTO createRecommendation(RecommendationDTO dto) {
        log.debug("createRecommendation: tries to create recommendations for the product with productId: {}", dto.getProductId());
       return recommendationService.createRecommendation(dto);
    }

    @Override
    public void deleteRecommendations(String id) {
        log.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", id);
        recommendationService.deleteRecommendations(id);
    }
    @Override
    public List<RecommendationDTO> getAllRecommendations() {
        log.debug("getAllRecommendations: tries to get all recommendations");
        return recommendationService.getAllRecommendations();
    }
}
