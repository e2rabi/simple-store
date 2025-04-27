package ma.errabi.microservice.composite.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.composite.service.ProductCompositeIntegration;
import ma.errabi.sdk.api.common.CustomPage;
import ma.errabi.sdk.api.composite.ProductAggregateDTO;
import ma.errabi.sdk.api.composite.ProductCompositeResources;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.review.ReviewDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ProductCompositeResource implements ProductCompositeResources {

    private final ProductCompositeIntegration integration;

    @Override
    @PostMapping(value    = "/product-composite/product",consumes = "application/json")
    public Mono<ProductDTO> createProduct(@RequestBody ProductDTO body) {
        return integration.createProduct(body);
    }
    @DeleteMapping(value = "/product-composite/product/{productId}")
    public Mono<Void> deleteProduct(@PathVariable String productId) {
         integration.deleteProduct(productId);
            return Mono.empty();
    }
    @Override
    @GetMapping(value = "/product-composite/product/{productId}", produces = "application/json")
    public Mono<ProductDTO> getProductById(@PathVariable String productId) {
       return integration.getProductById(productId);
    }
    @GetMapping(value = "/product-composite/product")
    public Mono<CustomPage<ProductDTO>> getProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        return integration.getAllProducts(page, pageSize);
    }
    @GetMapping(value = "/product-composite/product-details/{productId}")
    public ProductAggregateDTO getProductAggregate(@PathVariable String productId) {
        return integration.getProductAggregate(productId);
    }
    @PostMapping(value = "/product-composite/review",consumes = "application/json")
    public ReviewDTO createReview(@RequestBody ReviewDTO body) {
        log.info("Creating review: {}",body);
        return integration.createReview(body);
    }
    @GetMapping(value = "/product-composite/product/{productId}/review")
    public CustomPage<ReviewDTO> getproductReviews(@PathVariable String productId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        return integration.getProductReviews(productId,page,pageSize);
    }
    @DeleteMapping(value = "/product-composite/product/{productId}/review")
    public void deleteReviews(@PathVariable String productId) {
        integration.deleteReviews(productId);
    }
    @PostMapping(value = "/product-composite/recommendation",consumes = "application/json")
    public RecommendationDTO createProductRecommendation(@RequestBody RecommendationDTO body) {
        return integration.createRecommendation(body);
    }
    @DeleteMapping(value = "/product-composite/product/{productId}/recommendation")
    public void deleteRecommendation(@PathVariable String productId) {
        integration.deleteRecommendations(productId);
    }
    @GetMapping(value = "/product-composite/product/{productId}/recommendation")
    public CustomPage<RecommendationDTO> getRecommendationByProductId(@PathVariable String productId) {
        return integration.getRecommendationByProductId(productId);
    }
}
