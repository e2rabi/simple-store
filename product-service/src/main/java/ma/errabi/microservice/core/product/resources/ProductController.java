package ma.errabi.microservice.core.product.resources;

import lombok.RequiredArgsConstructor;
import ma.errabi.microservice.core.product.service.ProductService;
import ma.errabi.sdk.api.common.CustomPage;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.product.ProductResource;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductResource {

    private final ProductService productService;

    @Override
    public Mono<ProductDTO> getProductById(String productId) {
       return productService.getProductById(productId);
    }
    @Override
    public Mono<Void> deleteProduct(String productId) {
      return productService.deleteProduct(productId);
    }
    @Override
    public Mono<ProductDTO> createProduct(ProductDTO body) {
        return productService.createProduct(body);
    }
    @Override
    public Mono<CustomPage<ProductDTO>> getAllProducts(int pageNumber, int pageSize) {
        return productService.getAllProducts(pageNumber, pageSize);
    }
}
