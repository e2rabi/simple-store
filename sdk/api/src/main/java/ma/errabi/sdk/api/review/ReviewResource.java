package ma.errabi.sdk.api.review;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ReviewResource {
    @GetMapping(value = "/review/{productId}",produces = "application/json")
    List<ReviewDTO> getReview(String productId);

}
