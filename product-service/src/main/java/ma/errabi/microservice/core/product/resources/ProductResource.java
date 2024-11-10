package ma.errabi.microservice.core.product.resources;

import ma.errabi.sdk.api.product.ProductDto;
import ma.errabi.sdk.api.product.ProductService;
import ma.errabi.sdk.util.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductResource implements ProductService {

    private final ServiceUtil serviceUtil ;

    public ProductResource(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public ProductDto getProductById(Integer productId) {
        return new ProductDto(productId,"name","description",
                123,serviceUtil.getServiceAddress());
    }
}
