package ma.errabi.microservice.core.product.resources;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.product.service.ProductService;
import ma.errabi.sdk.api.common.CustomPage;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.product.ProductResource;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductResource {

    private final ProductService productService;

    @Override
    public Mono<ProductDTO> getProductById(String productId) {
       log.debug("getProductById: productId={}", productId);
       return productService.getProductById(productId);
    }
    @Override
    public Mono<Void> deleteProduct(String productId) {
      log.debug("deleteProduct: productId={}", productId);
      return productService.deleteProduct(productId);
    }
    @Override
    public Mono<ProductDTO> createProduct(ProductDTO body) {
        log.debug("createProduct: body={}", body);
        return productService.createProduct(body);
    }
    @Override
    public Mono<CustomPage<ProductDTO>> getAllProducts(int pageNumber, int pageSize) {
        log.debug("getAllProducts: pageNumber={}, pageSize={}", pageNumber, pageSize);
        return productService.getAllProducts(pageNumber, pageSize);
    }
}
