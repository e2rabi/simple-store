package ma.errabi.microservice.core.review.repository;

import ma.errabi.microservice.core.review.domain.ReviewEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface ReviewRepository extends ReactiveCrudRepository<ReviewEntity, Integer> {
    Flux<ReviewEntity> findByProductId(Integer productId, Pageable pageable);
    Flux<ReviewEntity> findByProductId(Integer productId);
}
