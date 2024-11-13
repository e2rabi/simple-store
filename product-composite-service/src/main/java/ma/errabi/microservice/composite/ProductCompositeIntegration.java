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
    private final String productServiceHost;
    private final int productServicePort;
    private final String productReviewServiceHost;
    private final int productReviewServicePort ;
    private final String productRecommendationServiceHost;
    private final int productRecommendationServicePort;

    public ProductCompositeIntegration(RestTemplate restTemplate,
                                       ObjectMapper objectMapper,
                                       @Value("${app.product-service.host}")
                                       String productServiceHost,
                                       @Value("${app.product-service.port}")
                                       int productServicePort,
                                       @Value("${app.product-review-service.host}")
                                       String productReviewServiceHost,
                                       @Value("${app.product-review-service.port}")
                                       int productReviewServicePort,
                                       @Value("${app.product-recommendation-service.host}")
                                       String productRecommendationServiceHost,
                                       @Value("${app.product-recommendation-service.port}")
                                       int productRecommendationServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.productServiceHost = String.format("%s:%d", productServiceHost, productServicePort);
        this.productServicePort = productServicePort;
        this.productReviewServiceHost = String.format("%s:%d", productReviewServiceHost, productReviewServicePort);
        this.productRecommendationServiceHost = String.format("%s:%d", productRecommendationServiceHost, productRecommendationServicePort);
        this.productReviewServicePort = productReviewServicePort;
        this.productRecommendationServicePort = productRecommendationServicePort;
    }
    @Override
    public ProductDTO getProductById(Integer productId) {
        String url = String.format(productServiceHost + "/product/%d", productId);
        return restTemplate.getForObject(url, ProductDTO.class);
    }
}
