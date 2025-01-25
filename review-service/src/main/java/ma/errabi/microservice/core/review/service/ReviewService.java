package ma.errabi.microservice.core.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.review.domain.Review;
import ma.errabi.microservice.core.review.mapper.ReviewMapper;
import ma.errabi.microservice.core.review.repository.ReviewRepository;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public void deleteReviews(String productId) {
        log.debug("Deleting all reviews for productId: {}", productId);
        List<Review> reviews =  reviewRepository.getReviewByProductId(productId);
        if(reviews.isEmpty()) {
            log.error("No reviews found for productId: {}", productId);
            throw new EntityNotFoundException("No reviews found for productId: " + productId);
        }
        reviewRepository.deleteAll(reviews);
    }
    public ReviewDTO saveReview(ReviewDTO review) {
        log.debug("Saving review: {}", review);
        return reviewMapper.toDTO(reviewRepository.save(reviewMapper.toEntity(review)));

    }
    public Page<ReviewDTO> getProductReviews(String productId, Pageable pageable) {
        log.debug("Getting all reviews for productId: {}", productId);
        return reviewRepository.getReviewByProductId(productId, pageable)
                .map(reviewMapper::toDTO);
    }
}
