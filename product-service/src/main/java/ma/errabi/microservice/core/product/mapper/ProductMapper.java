package ma.errabi.microservice.core.product.mapper;

import ma.errabi.microservice.core.product.domain.ProductEntity;
import ma.errabi.sdk.api.product.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductEntity toEntity(ProductDTO dto);
    ProductDTO toDTO(ProductEntity entity);
}
