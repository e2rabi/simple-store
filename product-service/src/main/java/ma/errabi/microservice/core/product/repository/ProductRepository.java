package ma.errabi.microservice.core.product.repository;

import ma.errabi.microservice.core.product.domain.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends  ReactiveCrudRepository<ProductEntity, String> {
    Flux<ProductEntity> findAllBy(Pageable pageable);
}