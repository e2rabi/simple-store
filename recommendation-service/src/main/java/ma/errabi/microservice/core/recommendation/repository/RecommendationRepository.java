package ma.errabi.microservice.core.recommendation.repository;

import ma.errabi.microservice.core.recommendation.domain.RecommendationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RecommendationRepository extends CrudRepository<RecommendationEntity, String> {
    List<RecommendationEntity> findByProductId(Integer productId);
}
