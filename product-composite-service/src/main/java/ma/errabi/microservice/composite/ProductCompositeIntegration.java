package ma.errabi.microservice.composite;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.product.ProductResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductCompositeIntegration implements ProductResource {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String productServiceUrl;
    private final String productReviewServiceUrl;
    private final String productRecommendationServiceUrl;

    public ProductCompositeIntegration(RestTemplate restTemplate,
                                       ObjectMapper objectMapper,
                                       @Value("${app.product-service.host}")
                                       String productServiceUrl,
                                       @Value("${app.product-review-service.host}")
                                       String productReviewServiceUrl,
                                       @Value("${app.product-recommendation-service.host}")
                                       String productRecommendationServiceUrl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.productServiceUrl = productServiceUrl;
        this.productReviewServiceUrl = productReviewServiceUrl;
        this.productRecommendationServiceUrl = productRecommendationServiceUrl;
    }
    @Override
    public ProductDTO getProductById(Integer productId) {
        return null;
    }
}
