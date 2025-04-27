package ma.errabi.microservice.core.review.repository;

import ma.errabi.microservice.core.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> getReviewByProductId(String productId, Pageable pageable);
    List<Review> getReviewByProductId(String productId);
}
