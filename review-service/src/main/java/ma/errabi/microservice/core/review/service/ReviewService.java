package ma.errabi.microservice.core.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.review.mapper.ReviewMapper;
import ma.errabi.microservice.core.review.repository.ReviewRepository;
import ma.errabi.sdk.api.review.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public void deleteReviews(String productId) {
        log.debug("Deleting all reviews for productId: {}", productId);
        reviewRepository.deleteReviewByProductId(productId);
    }
    public ReviewDTO saveReview(ReviewDTO review) {
        log.debug("Saving review: {}", review);
        return reviewMapper.toDTO(reviewRepository.save(reviewMapper.toEntity(review)));

    }
    public Page<ReviewDTO> getAllReviews(int productId, Pageable pageable) {
        log.debug("Getting all reviews for productId: {}", productId);
        return reviewRepository.getReviewByProductId(productId, pageable)
                .map(reviewMapper::toDTO);
    }
    public ReviewDTO getReview(int reviewId) {
        log.debug("Getting review with id: {}", reviewId);
        return reviewMapper.toDTO(reviewRepository.findById(reviewId).orElseThrow());
    }
}
