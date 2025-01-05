package ma.errabi.microservice.composite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.sdk.api.common.CustomPage;
import ma.errabi.sdk.api.composite.ProductAggregateDTO;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.product.ProductResource;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.recommendation.RecommendationResource;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.api.review.ReviewResource;
import ma.errabi.sdk.util.exception.EntityNotFoundException;
import ma.errabi.sdk.util.exception.TechnicalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


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
        return webClient.get().uri(url)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.error("Product with productId: {} not found", productId);
                    return Mono.error(new EntityNotFoundException("Product with productId: " + productId + " not found"));
                });
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
        return webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Create product failed: {}", ex.toString());
                    return Mono.error(new TechnicalException(ex.getMessage()));
                });
    }
    @Override
    public Mono<CustomPage<ProductDTO>> getAllProducts(int pageNumber, int pageSize) {
        String url = String.format("%s/products?page=%d&pageSize=%d", productServiceUrl, pageNumber, pageSize);
        log.debug("Will call the getAllProducts API on URL: {}", url);
        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public Mono<Void> deleteProduct(String productId) {
        String url = String.format("%s/product/%s", productServiceUrl, productId);
        deleteRecommendations(productId);
        log.debug("Will call the deleteProduct API on URL: {}", url);
        var response =  webClient.delete().uri(url).retrieve().bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.error("Delete failed product with productId: {} not found", productId);
                    return Mono.error(new EntityNotFoundException("Product with productId: " + productId + " not found"));
                });
        deleteRecommendations(productId);
        return response;
    }

    @Override
    public RecommendationDTO getRecommendations(String id,String productId) {
      String url = String.format("%s/recommendation/%s/product/%s", productRecommendationServiceHost, id,productId);
      log.debug("Will call the getRecommendations API on URL: {}", url);
        return webClient.get().uri(url)
                .retrieve()
                .bodyToMono(RecommendationDTO.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.error("Recommendation with productId: {} not found", id);
                    return Mono.error(new EntityNotFoundException("Recommendation with productId: " + id + " not found"));
                }).block();
    }
    public CustomPage<RecommendationDTO> getRecommendationByProductId(String productId) {
        String url = String.format("%s/recommendation/product/%s", productRecommendationServiceHost, productId);
        log.debug("Will call the getRecommendationByProductId API on URL: {}", url);
        return webClient.get().uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomPage<RecommendationDTO>>() {
                })
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.error("error getting recommendation by productId: {} not found", productId);
                    return Mono.error(new EntityNotFoundException("Recommendation with productId: " + productId + " not found"));
                }).block();
    }

    @Override
    public RecommendationDTO createRecommendation(RecommendationDTO dto) {
        String url = String.format("%s/recommendation", productRecommendationServiceHost);
        log.debug("Will post a new recommendation to URL: {}", url);
        return webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(RecommendationDTO.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Create recommendation failed: {}", ex.toString());
                    return Mono.error(new TechnicalException(ex.getMessage()));
                }).block();
    }

    public ProductAggregateDTO getProductAggregate(String productId) {
        ProductDTO product = getProductById(productId).block();
        CustomPage<RecommendationDTO> recommendations = getRecommendationByProductId(productId);
        return  ProductAggregateDTO.builder()
                .recommendations(recommendations.getContent())
                .productId(product.getProductId())
                .name(product.getName())
                .weight(product.getWeight())
                .description(product.getDescription())
                .build();
    }
    @Override
    public void deleteRecommendations(String id) {
        String url = String.format("%s/recommendation/%s", productRecommendationServiceHost, id);
        log.debug("Will call the deleteRecommendations API on URL: {}", url);
        webClient.delete().uri(url).retrieve().bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.error("Delete failed recommendation with id: {} not found", id);
                    return Mono.error(new EntityNotFoundException("Recommendation with id: " + id + " not found"));
                }).block();
    }

    @Override
    public List<RecommendationDTO> getAllRecommendations() {
        return List.of();
    }

    @Override
    public CustomPage<RecommendationDTO> getRecommendationByRating(Integer minRating, Integer maxRating) {
        return null;
    }
}