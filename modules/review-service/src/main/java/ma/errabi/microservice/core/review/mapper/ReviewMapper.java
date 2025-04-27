package ma.errabi.microservice.core.review.mapper;

import ma.errabi.microservice.core.review.domain.Review;
import ma.errabi.sdk.api.review.ReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    @Mapping(target = "id", ignore = true)
    Review toEntity(ReviewDTO dto);
    ReviewDTO toDTO(Review entity);
}
