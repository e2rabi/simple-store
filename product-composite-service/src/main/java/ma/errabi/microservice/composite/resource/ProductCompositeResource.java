package ma.errabi.microservice.composite.resource;

import ma.errabi.microservice.composite.service.ProductCompositeIntegration;
import ma.errabi.sdk.api.composite.ProductAggregateDTO;
import ma.errabi.sdk.api.composite.ProductCompositeResources;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.util.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductCompositeResource implements ProductCompositeResources {
    private final ProductCompositeIntegration integration;
    private final ServiceUtil serviceUtil;

    public ProductCompositeResource(ProductCompositeIntegration integration, ServiceUtil serviceUtil) {
        this.integration = integration;
        this.serviceUtil = serviceUtil;
    }
    @Override
    public ProductAggregateDTO getProductById(Integer productId) {
       ProductDTO productDTO =  integration.getProductById(productId);
       List<RecommendationDTO>  recommendationDTOS = integration.getRecommendations(productId);
       List<ReviewDTO> reviewDTOS = integration.getReview(productId);
       return serviceUtil.createProductAggregate(productDTO, recommendationDTOS, reviewDTOS);
    }
}
