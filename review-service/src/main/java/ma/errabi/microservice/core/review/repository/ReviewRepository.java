package ma.errabi.microservice.core.review.repository;

import ma.errabi.microservice.core.review.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface ReviewRepository extends ReactiveCrudRepository<Review, Integer> {
    Flux<Review> findByProductId(Integer productId, Pageable pageable);
    Flux<Review> findByProductId(Integer productId);
}
