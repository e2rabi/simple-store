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
import ma.errabi.sdk.util.exception.TechnicalException;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class ProductCompositeIntegration implements ProductResource, RecommendationResource, ReviewResource {
    private final WebClient webClient;
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
        this.productServiceUrl = String.format("%s:%d", productServiceHost, productServicePort);
        this.productReviewServiceHost = String.format("%s:%d", productReviewServiceHost, productReviewServicePort);
        this.productRecommendationServiceHost = String.format("%s:%d", productRecommendationServiceHost, productRecommendationServicePort);
    }

    @Override
    public Mono<ProductDTO> getProductById(String productId) {
        String url = String.format("%s/product/%s", productServiceUrl, productId);
        log.debug("Will call the getProduct API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(ProductDTO.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.error("Product with productId: {} not found", productId);
                    return Mono.error(new EntityNotFoundException("Product with productId: " + productId + " not found"));
                });
    }

    @Override
    public Flux<RecommendationDTO> getRecommendations(String productId) {
        String url = String.format("%s/recommendation/%s", productRecommendationServiceHost, productId);
        return webClient.get().uri(url).retrieve().bodyToFlux(RecommendationDTO.class);
    }

    @Override
    public Mono<RecommendationDTO> createRecommendation(RecommendationDTO dto) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public Mono<Void> deleteRecommendations(String productId) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public Flux<ReviewDTO> getReview(String productId) {
        String url = String.format("%s/review/%d", productReviewServiceHost, productId);
        return webClient.get().uri(url).retrieve().bodyToFlux(ReviewDTO.class);
    }

    @Override
    public Mono<ProductDTO> createProduct(ProductDTO body) {
        String url = String.format("%s/product", productServiceUrl);
        log.debug("Will post a new product to URL: {}", url);
        return webClient.post().uri(url).bodyValue(body).retrieve().bodyToMono(ProductDTO.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Create product failed: {}", ex.toString());
                    return Mono.error(new TechnicalException(ex.getMessage()));
                });
    }

    @Override
    public Mono<Page<ProductDTO>> getAllProducts(int pageNumber, int pageSize) {
        String url = String.format("%s?page=%d&pageSize=%d", productServiceUrl, pageNumber, pageSize);
        log.debug("Will call the getAllProducts API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<Void> deleteProduct(String productId) {
        String url = String.format("%s/product/%s", productServiceUrl, productId);
        log.debug("Will call the deleteProduct API on URL: {}", url);
        return webClient.delete().uri(url).retrieve().bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.error("Delete failed product with productId: {} not found", productId);
                    return Mono.error(new EntityNotFoundException("Product with productId: " + productId + " not found"));
                });
    }
}