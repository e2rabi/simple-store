package ma.errabi.microservice.composite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.sdk.api.composite.ProductAggregateDTO;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.product.ProductResource;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.recommendation.RecommendationResource;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.api.review.ReviewResource;
import ma.errabi.sdk.util.exception.EntityNotFoundException;
import ma.errabi.sdk.util.exception.HttpErrorInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ProductCompositeIntegration implements ProductResource, RecommendationResource, ReviewResource {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String productServiceUrl;
    private final String productReviewServiceHost;
    private final String productRecommendationServiceHost;

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
        this.productServiceUrl = String.format("%s:%d", productServiceHost, productServicePort);
        this.productReviewServiceHost = String.format("%s:%d", productReviewServiceHost, productReviewServicePort);
        this.productRecommendationServiceHost = String.format("%s:%d", productRecommendationServiceHost, productRecommendationServicePort);
    }
    @Override
    public ProductDTO getProductById(Integer productId) {
        String url = String.format("%s/product/%d", productServiceUrl, productId);
        log.info("url used to get product by id: {}", url);
        return restTemplate.getForObject(url, ProductDTO.class);
    }

    @Override
    public void deleteProduct(Integer productId) {

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
    public ProductDTO createProduct(ProductAggregateDTO body) {
        try {
            String url = productServiceUrl;
            log.debug("Will post a new product to URL: {}", url);

            ProductDTO product = restTemplate.postForObject(url, body, ProductDTO.class);
            assert product != null;
            log.debug("Created a product with id: {}", product.getProductId());

            return product;

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }
    public void deleteProduct(int productId) {
        try {
            String url = productServiceUrl + "/" + productId;
            log.debug("Will call the deleteProduct API on URL: {}", url);

            restTemplate.delete(url);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }
    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (Objects.requireNonNull(HttpStatus.resolve(ex.getStatusCode().value()))) {
            case NOT_FOUND, UNPROCESSABLE_ENTITY:
                return new EntityNotFoundException(getErrorMessage(ex));
            default:
                log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                log.warn("Error body: {}", ex.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException e) {
            return ex.getMessage();
        }
    }
}
