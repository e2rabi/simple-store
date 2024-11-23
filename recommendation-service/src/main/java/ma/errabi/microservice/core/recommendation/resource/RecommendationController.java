package ma.errabi.microservice.core.recommendation.resource;

import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.recommendation.RecommendationResource;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationController implements RecommendationResource {
    @Override
    public List<RecommendationDTO> getRecommendations(Integer productId) {
        return List.of(new RecommendationDTO(1, "Author 1",  "Content 1"),
                new RecommendationDTO(2, "Author 2",  "Content 2"));
    }
}
