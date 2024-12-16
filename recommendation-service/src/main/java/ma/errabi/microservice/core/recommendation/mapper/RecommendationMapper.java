package ma.errabi.microservice.core.recommendation.mapper;

import ma.errabi.microservice.core.recommendation.domain.RecommendationEntity;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecommendationMapper {

    RecommendationEntity toEntity(RecommendationDTO dto);
    RecommendationDTO toDTO(RecommendationEntity entity);
}
