package ma.errabi.sdk.api.review;

import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;


public interface ReviewResource {
    @GetMapping(value = "/review/{productId}",produces = "application/json")
    Flux<ReviewDTO> getReview(String productId);

}
