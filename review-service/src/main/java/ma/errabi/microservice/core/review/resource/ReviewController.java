package ma.errabi.microservice.core.review.resource;

import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.api.review.ReviewResource;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReviewController implements ReviewResource {
    @Override
    public List<ReviewDTO> getReview(Integer productId) {
        return List.of(new ReviewDTO(1, "Review 1", "This is a review for product 1"),
                new ReviewDTO(2, "Review 2", "This is a review for product 1"));
    }
}
