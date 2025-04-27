package ma.errabi.microservice.core.recommendation.repository;

import ma.errabi.microservice.core.recommendation.domain.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation,String> {

    @Modifying
    @Query("DELETE FROM Recommendation r WHERE r.productId = ?1")
    void deleteByProductId(String productId);
    Page<Recommendation> findByProductId(String productId, Pageable pageable);
    Optional<Recommendation> findByProductId(String productId);
}
