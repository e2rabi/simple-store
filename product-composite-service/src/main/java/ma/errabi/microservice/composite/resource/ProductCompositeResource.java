package ma.errabi.microservice.composite.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.composite.service.ProductCompositeIntegration;
import ma.errabi.sdk.api.composite.ProductCompositeResources;
import ma.errabi.sdk.api.product.ProductDTO;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductCompositeResource implements ProductCompositeResources {
    private final ProductCompositeIntegration integration;

    @Override
    public Mono<ProductDTO> getProductById(String productId) {
       return integration.getProductById(productId);
    }

    @Override
    public Mono<ProductDTO> createProduct(ProductDTO body) {
      return integration.createProduct(body);
    }
    public Mono<Void> deleteProduct(String productId) {
       return integration.deleteProduct(productId);
    }
}
