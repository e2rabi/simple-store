package ma.errabi.microservice.composite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Component
public class ProductCompositeIntegration implements ProductResource, RecommendationResource, ReviewResource {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String productServiceUrl;
    private final String productReviewServiceHost;
    private final String productRecommendationServiceHost;

    public ProductCompositeIntegration(WebClient webClient, ObjectMapper objectMapper,
                                       @Value("${app.product-service.host}") String productServiceHost,
                                       @Value("${app.product-service.port}") int productServicePort,
                                       @Value("${app.review-service.host}") String productReviewServiceHost,
                                       @Value("${app.review-service.port}") int productReviewServicePort,
                                       @Value("${app.recommendation-service.host}") String productRecommendationServiceHost,
                                       @Value("${app.recommendation-service.port}") int productRecommendationServicePort) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.productServiceUrl = String.format("%s:%d", productServiceHost, productServicePort);
        this.productReviewServiceHost = String.format("%s:%d", productReviewServiceHost, productReviewServicePort);
        this.productRecommendationServiceHost = String.format("%s:%d", productRecommendationServiceHost, productRecommendationServicePort);
    }

    @Override
    public Mono<ProductDTO> getProductById(String productId) {
        String url = String.format("%s/product/%s", productServiceUrl, productId);
        log.info("url used to get product by id: {}", url);
        return null;
    }

    @Override
    public Flux<RecommendationDTO> getRecommendations(String productId) {
        String url = String.format("%s/recommendation/%s", productRecommendationServiceHost, productId);
        return webClient.get().uri(url).retrieve().bodyToFlux(RecommendationDTO.class);
    }

    @Override
    public Mono<RecommendationDTO> createRecommendation(RecommendationDTO dto) {
        return null;
    }

    @Override
    public Mono<Void> deleteRecommendations(String productId) {
        return null;
    }

    @Override
    public Flux<ReviewDTO> getReview(String productId) {
        String url = String.format("%s/review/%d", productReviewServiceHost, productId);
        return webClient.get().uri(url).retrieve().bodyToFlux(ReviewDTO.class);
    }

    @Override
    public Mono<ProductDTO> createProduct(ProductDTO body) {
        String url = productServiceUrl;
        log.debug("Will post a new product to URL: {}", url);
        return webClient.post().uri(url).bodyValue(body).retrieve().bodyToMono(ProductDTO.class)
                .onErrorMap(HttpClientErrorException.class, this::handleHttpClientException);
    }

    @Override
    public Mono<Page<ProductDTO>> getAllProducts(int pageNumber, int pageSize) {
        String url = String.format("%s?page=%d&pageSize=%d", productServiceUrl, pageNumber, pageSize);
        log.debug("Will call the getAllProducts API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<Void> deleteProduct(String productId) {
        String url = productServiceUrl + "/" + productId;
        log.debug("Will call the deleteProduct API on URL: {}", url);
        return webClient.delete().uri(url).retrieve().toBodilessEntity().then()
                .onErrorMap(HttpClientErrorException.class, this::handleHttpClientException);
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == HttpStatus.NOT_FOUND || status == HttpStatus.UNPROCESSABLE_ENTITY) {
            return new EntityNotFoundException(getErrorMessage(ex));
        }
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException e) {
            return ex.getMessage();
        }
    }
}