package ma.errabi.microservice.core.recommendation.repository;

import ma.errabi.microservice.core.recommendation.domain.Recommendation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RecommendationRepository extends ReactiveCrudRepository<Recommendation, String> {
    Flux<Recommendation> findByProductId(String productId);
}
