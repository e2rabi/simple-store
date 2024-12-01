package ma.errabi.microservice.core.product.mapper;

import ma.errabi.microservice.core.product.domain.ProductEntity;
import ma.errabi.sdk.api.product.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id",source = "productId")
    ProductEntity toEntity(ProductDTO dto);
    @Mapping(target = "productId",source = "id")
    ProductDTO toDTO(ProductEntity entity);
}
