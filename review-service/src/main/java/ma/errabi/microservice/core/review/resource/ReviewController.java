package ma.errabi.microservice.core.review.resource;

import lombok.RequiredArgsConstructor;
import ma.errabi.microservice.core.review.service.ReviewService;
import ma.errabi.sdk.api.review.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {//implements ReviewResource {
    private final ReviewService reviewService;

    @PostMapping
    public ReviewDTO createReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.saveReview(reviewDTO);
    }

    @DeleteMapping("/{productId}")
    public void deleteReviews(@PathVariable String productId) {
         reviewService.deleteReviews(productId);
    }

    @GetMapping("/{productId}/product")
    public Page<ReviewDTO> getProductReviews(@PathVariable String productId,
                                             @RequestParam(defaultValue = "0")int page,
                                             @RequestParam(defaultValue = "10") int pageSize,
                                             @RequestParam(required = false) String sortBy,
                                             @RequestParam(required = false) String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "ASC"), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return reviewService.getProductReviews(productId, pageable);
    }
}
