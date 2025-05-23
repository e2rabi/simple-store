package ma.errabi.microservice.composite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.sdk.api.common.CustomPage;
import ma.errabi.sdk.api.composite.ProductAggregateDTO;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.event.Event;
import ma.errabi.sdk.event.EventPublisher;
import ma.errabi.sdk.exception.EntityNotFoundException;
import ma.errabi.sdk.exception.TechnicalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCompositeIntegration  {
    private final WebClient.Builder webClient;
    private final EventPublisher eventPublisher;
    private final RestTemplate restTemplate;

    @Value("${app.product-service.host}")
    private  String productServiceUrl;
    @Value("${app.review-service.host}")
    private  String productReviewServiceUrl;
    @Value("${app.recommendation-service.host}")
    private  String productRecommendationServiceUrl; ;

    public Mono<ProductDTO> createProduct(ProductDTO body) {
        String url = String.format("%s/product", productServiceUrl);
        log.debug("Calling create product API : {}", url);
        return webClient.build().post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Create product failed: {}", ex.toString());
                    return Mono.error(new TechnicalException(ex.getMessage()));
                });
    }
    public Mono<ProductDTO> getProductById(String productId) {
        String url = String.format("%s/product/%s", productServiceUrl, productId);
        log.debug("Call get product by product id API on URL: {}", url);
        return webClient.build().get().uri(url)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .onErrorResume(Exception.class, ex -> {
                    log.error("Product with productId: {} not found", productId);
                    return Mono.error(new EntityNotFoundException("Product with productId: " + productId + " not found"));
                });
    }
    public void deleteProduct(String productId) {
        // refactor this using saga pattern
        log.debug("Publish event delete product details by product id {}", productId);
        ProductDTO productDTO =   getProductById(productId).block();

        if (productDTO == null) {
            throw new EntityNotFoundException("Product with productId: " + productId + " not found");
        }
        Event<String, ProductDTO> event = new Event<>(productDTO, productDTO.getProductId(), Event.Type.DELETE);
        eventPublisher.publishEvent(event);

        Event<String, RecommendationDTO> deleteRecommendationEvent = new Event<>(RecommendationDTO.builder().productId(productId).build(), productId, Event.Type.DELETE);
        eventPublisher.publishEvent(deleteRecommendationEvent);

        Event<String, ReviewDTO> deleteReviewEvent = new Event<>(ReviewDTO.builder().productId(productId).build(), productId, Event.Type.DELETE);
        eventPublisher.publishEvent(deleteReviewEvent);

    }


    public Flux<ReviewDTO> getReview(String productId) {
        String url = String.format("%s/review/%s", productReviewServiceUrl, productId);
        return webClient.build().get().uri(url).retrieve().bodyToFlux(ReviewDTO.class);
    }
    public Mono<CustomPage<ProductDTO>> getAllProducts(int pageNumber, int pageSize) {
        String url = String.format("%s/product?page=%d&pageSize=%d", productServiceUrl, pageNumber, pageSize);
        log.debug("Will call the getAllProducts API on URL: {}", url);
        return webClient.build().get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
    public CustomPage<RecommendationDTO> getRecommendationByProductId(String productId) {
        String url = String.format("%s/recommendation/product/%s", productRecommendationServiceUrl, productId);
        log.debug("Will call the getRecommendationByProductId API on URL: {}", url);
        return webClient.build().get().uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomPage<RecommendationDTO>>() {
                })
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.error("error getting recommendation by productId: {} not found", productId);
                    return Mono.error(new EntityNotFoundException("Recommendation with productId: " + productId + " not found"));
                }).block();
    }

    public RecommendationDTO createRecommendation(RecommendationDTO body) {
        String url = String.format("%s/recommendation", productRecommendationServiceUrl);
        log.debug("Call create a recommendation API to URL: {}", url);
        return webClient.build().post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(RecommendationDTO.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Create recommendation failed: {}", ex.toString());
                    return Mono.error(new TechnicalException(ex.getMessage()));
                }).block();
    }

    public ProductAggregateDTO getProductAggregate(String productId) {
        ProductDTO product = getProductById(productId).block();
        if(product == null) {
            throw new EntityNotFoundException("Product with productId: " + productId + " not found");
        }
        CustomPage<RecommendationDTO> recommendations = getRecommendationByProductId(productId);
        List<ReviewDTO> reviewDTOS =  getReview(productId).collectList().block();
        return  ProductAggregateDTO.builder()
                .recommendations(recommendations.getContent())
                .productId(product.getProductId())
                .reviews(reviewDTOS)
                .name(product.getName())
                .weight(product.getWeight())
                .description(product.getDescription())
                .build();
    }
    public void deleteRecommendations(String productId) {
        String url = String.format("%s/recommendation/product/%s", productRecommendationServiceUrl,productId);
        log.debug("Will call the deleteRecommendations API on URL: {}", url);
        webClient.build().delete().uri(url).retrieve().bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.error("Delete failed recommendation with product id: {} not found", productId);
                    return Mono.error(new EntityNotFoundException("Recommendation with product id: " + productId + " not found"));
                }).block();
    }

    public ReviewDTO createReview(ReviewDTO body) {
        String url = String.format("%s/review", productReviewServiceUrl);
        log.info("Will post a new review to URL: {}", url);
        return restTemplate.postForObject(url, body, ReviewDTO.class);
    }
    public CustomPage<ReviewDTO> getProductReviews(String productId, int page, int pageSize) {
        String url = String.format("%s/review/%s/product", productReviewServiceUrl, productId);
        log.debug("Call get review by product id API on URL: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("page", String.valueOf(page));
        headers.set("pageSize", String.valueOf(pageSize));

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<CustomPage<ReviewDTO>>() {}).getBody();
    }
    public void deleteReviews(String productId) {
        String url = String.format("%s/review/%s", productReviewServiceUrl, productId);
        log.debug("Call the deleteReviews API on URL: {}", url);
        try {
            restTemplate.delete(url);
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Failed to delete reviews for productId: {}", productId, ex);
            throw new EntityNotFoundException("Failed to delete reviews for productId: " + productId);
        }
    }
}