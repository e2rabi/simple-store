package ma.errabi.sdk.api.review;

import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;


public interface ReviewResource {
    Flux<ReviewDTO> getReview(String productId);

}
