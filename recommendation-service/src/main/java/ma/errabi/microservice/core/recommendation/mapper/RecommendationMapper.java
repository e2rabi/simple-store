package ma.errabi.microservice.core.recommendation.mapper;

import ma.errabi.microservice.core.recommendation.domain.Recommendation;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecommendationMapper {

    Recommendation toEntity(RecommendationDTO dto);
    RecommendationDTO toDTO(Recommendation entity);
}
