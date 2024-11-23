package ma.errabi.microservice.composite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.product.ProductResource;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.recommendation.RecommendationResource;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.api.review.ReviewResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ProductCompositeIntegration implements ProductResource, RecommendationResource, ReviewResource {
    Logger logger = Logger.getLogger(ProductCompositeIntegration.class.getName());
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
                                       @Value("${app.review-service.host}")
                                       String productReviewServiceHost,
                                       @Value("${app.review-service.port}")
                                       int productReviewServicePort,
                                       @Value("${app.recommendation-service.host}")
                                       String productRecommendationServiceHost,
                                       @Value("${app.recommendation-service.port}")
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
        String url = String.format("%s/product/%d",productServiceHost, productId);
        logger.info("url used to get product by id: " + url);
        return restTemplate.getForObject(url, ProductDTO.class);
    }
    @Override
    public List<RecommendationDTO> getRecommendations(Integer productId) {
        String url = String.format("%s/recommendation/%d",productRecommendationServiceHost, productId);
        ResponseEntity<List<RecommendationDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();
    }

    @Override
    public List<ReviewDTO> getReview(Integer productId) {
        String url = String.format("%s/review/%d", productReviewServiceHost, productId);
        ResponseEntity<List<ReviewDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();
    }
}
