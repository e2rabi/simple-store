package ma.errabi.microservice.core.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.review.mapper.ReviewMapper;
import ma.errabi.microservice.core.review.repository.ReviewRepository;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public void deleteReviews(int productId) {
        try {
            log.debug("Deleting all reviews for productId: {}", productId);
             reviewRepository.deleteById(productId);
        } catch (Exception e) {
            log.error("Failed to delete reviews for productId: {}", productId, e);
            throw new EntityNotFoundException("Failed to delete reviews for productId: " + productId);
        }
    }
    public ReviewDTO saveReview(ReviewDTO review) {
        return reviewMapper.toDTO(reviewRepository.save(reviewMapper.toEntity(review)));

    }
    public Page<ReviewDTO> getAllReviews(int productId, Pageable pageable) {
      return null;

    }
    public ReviewDTO getReview(String productId) {
        // reviewMapper.toDTO(reviewRepository.findByProductId(Integer.parseInt(productId)));
     return null;
    }
}
