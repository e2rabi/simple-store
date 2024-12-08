package ma.errabi.sdk.api.product;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

public interface ProductResource {

    @GetMapping(value = "/product/{productId}",produces = "application/json")
    ProductDTO getProductById(@PathVariable String productId);
    @DeleteMapping(value = "/product/{productId}")
    void deleteProduct(@PathVariable String productId);
    @PostMapping(value = "/product",consumes = "application/json")
    ProductDTO createProduct(@RequestBody ProductDTO body);
    @GetMapping(value = "/product",consumes = "application/json")
    Page<ProductDTO> getAllProducts(@RequestParam(defaultValue = "0") int pageNumber,
                                    @RequestParam(defaultValue = "10") int pageSize);
}
