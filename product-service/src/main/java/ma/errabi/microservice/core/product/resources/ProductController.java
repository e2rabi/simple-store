package ma.errabi.microservice.core.product.resources;

import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.product.ProductResource;
import ma.errabi.sdk.util.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductResource {

    private final ServiceUtil serviceUtil ;

    public ProductController(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public ProductDTO getProductById(Integer productId) {
        return new ProductDTO(productId,"name","description",
                123,serviceUtil.getServiceAddress());
    }
}
