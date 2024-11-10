package ma.errabi.sdk.api.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductResource {

    @GetMapping(value = "/product/{productId}",produces = "application/json")
    ProductDTO getProductById(@PathVariable Integer productId);
}
