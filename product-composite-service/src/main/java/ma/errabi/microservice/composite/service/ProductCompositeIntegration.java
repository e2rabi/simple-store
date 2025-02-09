package ma.errabi.microservice.composite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@Component
public class ProductCompositeIntegration  {
    private final WebClient webClient;
    private final String productServiceUrl;
    private final String productReviewServiceHost;
    private final String productRecommendationServiceHost;
    private final EventPublisher eventPublisher;
    private final RestTemplate restTemplate ;

    public ProductCompositeIntegration(WebClient webClient, ObjectMapper objectMapper,
                                       @Value("${app.product-service.host}") String productServiceHost,
                                       @Value("${app.product-service.port}") int productServicePort,
                                       @Value("${app.review-service.host}") String productReviewServiceHost,
                                       @Value("${app.review-service.port}") int productReviewServicePort,
                                       @Value("${app.recommendation-service.host}") String productRecommendationServiceHost,
                                       @Value("${app.recommendation-service.port}") int productRecommendationServicePort, StreamBridge streamBridge, EventPublisher eventPublisher, RestTemplate restTemplate) {
        this.webClient = webClient;
        this.eventPublisher = eventPublisher;
        this.restTemplate = restTemplate;
        this.productServiceUrl = String.format("%s:%d", productServiceHost, productServicePort);
        this.productReviewServiceHost = String.format("%s:%d", productReviewServiceHost, productReviewServicePort);
        this.productRecommendationServiceHost = String.format("%s:%d", productRecommendationServiceHost, productRecommendationServicePort);
    }
    public Mono<ProductDTO> createProduct(ProductDTO body) {
        String url = String.format("%s/product", productServiceUrl);
        log.debug("Calling create product API : {}", url);
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
    public Mono<ProductDTO> getProductById(String productId) {
        String url = String.format("%s/product/%s", productServiceUrl, productId);
        log.debug("Call get product by product id API on URL: {}", url);
        return webClient.get().uri(url)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .onErrorResume(Exception.class, ex -> {
                    log.error("Product with productId: {} not found", productId);
                    return Mono.error(new EntityNotFoundException("Product with productId: " + productId + " not found"));
                });
    }
    public void deleteProduct(String productId) {
        log.debug("Publish event delete product by product id {}", productId);
        ProductDTO productDTO =   getProductById(productId).block();
        assert productDTO != null;
        Event<String, ProductDTO> event = new Event<>(productDTO, productDTO.getProductId(), Event.Type.DELETE);
        eventPublisher.publishEvent(event);
        // publish delete review
        // publish delete recommendation
    }


    public Flux<ReviewDTO> getReview(String productId) {
        String url = String.format("%s/review/%s", productReviewServiceHost, productId);
        return webClient.get().uri(url).retrieve().bodyToFlux(ReviewDTO.class);
    }
    public Mono<CustomPage<ProductDTO>> getAllProducts(int pageNumber, int pageSize) {
        String url = String.format("%s/product?page=%d&pageSize=%d", productServiceUrl, pageNumber, pageSize);
        log.debug("Will call the getAllProducts API on URL: {}", url);
        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
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

    public RecommendationDTO createRecommendation(RecommendationDTO body) {
        String url = String.format("%s/recommendation", productRecommendationServiceHost);
        log.debug("Call create a recommendation API to URL: {}", url);
        return webClient.post().uri(url)
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
    public void deleteRecommendations(String productId, String id) {
        String url = String.format("%s/recommendation/%s/product/%s", productRecommendationServiceHost, id,productId);
        log.debug("Will call the deleteRecommendations API on URL: {}", url);
        webClient.delete().uri(url).retrieve().bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.error("Delete failed recommendation with id: {} not found", id);
                    return Mono.error(new EntityNotFoundException("Recommendation with id: " + id + " not found"));
                }).block();
    }

    public ReviewDTO createReview(ReviewDTO body) {
        String url = String.format("%s/review", productReviewServiceHost);
        log.info("Will post a new review to URL: {}", url);
        return restTemplate.postForObject(url, body, ReviewDTO.class);
    }
    public CustomPage<ReviewDTO> getProductReviews(String productId, int page, int pageSize) {
        String url = String.format("%s/review/%s/product", productReviewServiceHost, productId);
        log.debug("Call get review by product id API on URL: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("page", String.valueOf(page));
        headers.set("pageSize", String.valueOf(pageSize));

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<CustomPage<ReviewDTO>>() {}).getBody();
    }
    public void deleteReviews(String productId) {
        String url = String.format("%s/review/%s", productReviewServiceHost, productId);
        log.debug("Call the deleteReviews API on URL: {}", url);
        try {
            restTemplate.delete(url);
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Failed to delete reviews for productId: {}", productId, ex);
            throw new EntityNotFoundException("Failed to delete reviews for productId: " + productId);
        }
    }
}