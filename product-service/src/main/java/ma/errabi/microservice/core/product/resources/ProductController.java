package ma.errabi.microservice.core.product.resources;

import lombok.RequiredArgsConstructor;
import ma.errabi.microservice.core.product.service.ProductService;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.product.ProductResource;
import ma.errabi.sdk.util.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductResource {

    private final ServiceUtil serviceUtil ;
    private final ProductService productService;


    @Override
    public ProductDTO getProductById(String productId) {
       return productService.getProductById(productId);
    }

    @Override
    public void deleteProduct(String productId) {
      productService.deleteProduct(productId);
    }

    @Override
    public ProductDTO createProduct(ProductDTO body) {
        return productService.createProduct(body);
    }

}
