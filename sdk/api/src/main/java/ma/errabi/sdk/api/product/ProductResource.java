package ma.errabi.sdk.api.product;

import org.springframework.web.bind.annotation.*;

public interface ProductResource {

    @GetMapping(value = "/product/{productId}",produces = "application/json")
    ProductDTO getProductById(@PathVariable String productId);
    @DeleteMapping(value = "/product/{productId}")
    void deleteProduct(@PathVariable String productId);
    @PostMapping(value = "/product",consumes = "application/json")
    ProductDTO createProduct(@RequestBody ProductDTO body);
}
