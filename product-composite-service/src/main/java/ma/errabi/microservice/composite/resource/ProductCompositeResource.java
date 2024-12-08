package ma.errabi.microservice.composite.resource;

import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.composite.service.ProductCompositeIntegration;
import ma.errabi.sdk.api.composite.ProductAggregateDTO;
import ma.errabi.sdk.api.composite.ProductCompositeResources;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.util.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ProductCompositeResource implements ProductCompositeResources {
    private final ProductCompositeIntegration integration;
    private final ServiceUtil serviceUtil;

    public ProductCompositeResource(ProductCompositeIntegration integration, ServiceUtil serviceUtil) {
        this.integration = integration;
        this.serviceUtil = serviceUtil;
    }
    @Override
    public ProductAggregateDTO getProductById(String productId) {
       ProductDTO productDTO =  integration.getProductById(productId);
       List<RecommendationDTO>  recommendationDTOS = integration.getRecommendations(productId);
       List<ReviewDTO> reviewDTOS = integration.getReview(productId);
       return serviceUtil.createProductAggregate(productDTO, recommendationDTOS, reviewDTOS);
    }

    @Override
    public ProductDTO createProduct(ProductDTO body) {
      return   integration.createProduct(body);
    }
    public void deleteProduct(String productId) {

        log.debug("deleteCompositeProduct: Deletes a product aggregate for productId: {}", productId);

        integration.deleteProduct(productId);

       // integration.deleteRecommendations(productId);

      //  integration.deleteReviews(productId);

        log.debug("deleteCompositeProduct: aggregate entities deleted for productId: {}", productId);
    }
}
