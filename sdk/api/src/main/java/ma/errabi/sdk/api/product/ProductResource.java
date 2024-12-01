package ma.errabi.sdk.api.product;

import org.springframework.web.bind.annotation.*;

public interface ProductResource {

    @GetMapping(value = "/product/{productId}",produces = "application/json")
    ProductDTO getProductById(@PathVariable Integer productId);
    @DeleteMapping(value = "/product/{productId}")
    void deleteProduct(@PathVariable Integer productId);
}
