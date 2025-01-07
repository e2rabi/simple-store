package ma.errabi.microservice.core.review.resource;

import lombok.RequiredArgsConstructor;
import ma.errabi.microservice.core.review.service.ReviewService;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.api.review.ReviewResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewResource {
    private final ReviewService reviewService;

    @PostMapping
    public Mono<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
            return reviewService.saveReview(reviewDTO);
    }

    @DeleteMapping
    public Mono<Void> deleteReviews(int productId) {
        return reviewService.deleteReviews(productId);
    }

    @GetMapping("/review/{productId}/product")
    public Flux<Page<ReviewDTO>> getAllReviews(@PathVariable int productId,
                                               @RequestParam int page,
                                               @RequestParam int pageSize,
                                               @RequestParam(required = false) String sortBy,
                                               @RequestParam(required = false) String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "ASC"), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return reviewService.getAllReviews(productId, pageable);
    }

    @Override
    @GetMapping("/review/{productId}")
    public Flux<ReviewDTO> getReview(@PathVariable String productId) {
        return reviewService.getReview(productId);
    }
}
