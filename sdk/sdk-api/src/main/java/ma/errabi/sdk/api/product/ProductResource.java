package ma.errabi.sdk.api.product;

import ma.errabi.sdk.api.common.CustomPage;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

public interface ProductResource {

    @GetMapping(value = "/product/{productId}",produces = "application/json")
    Mono<ProductDTO> getProductById(@PathVariable String productId);
    @DeleteMapping(value = "/product/{productId}")
    Mono<Void> deleteProduct(@PathVariable String productId);
    @PostMapping(value = "/product",consumes = "application/json")
    Mono<ProductDTO> createProduct(@RequestBody ProductDTO body);
    @GetMapping(value = "/product")
    Mono<CustomPage<ProductDTO>> getAllProducts(@RequestParam(defaultValue = "0") int pageNumber,
                                                @RequestParam(defaultValue = "10") int pageSize);
}
