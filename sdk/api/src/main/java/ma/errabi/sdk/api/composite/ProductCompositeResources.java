package ma.errabi.sdk.api.composite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductCompositeResource {
    @GetMapping(value = "/product/{productId}",produces = "application/json")
    ProductAggregateDTO getProductById(@PathVariable Integer productId);
}
