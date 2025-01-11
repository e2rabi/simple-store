package ma.errabi.microservice.core.product.mapper;

import ma.errabi.microservice.core.product.domain.Product;
import ma.errabi.sdk.api.product.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product toEntity(ProductDTO dto);
    ProductDTO toDTO(Product entity);
}
