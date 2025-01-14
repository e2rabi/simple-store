package ma.errabi.microservice.core.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.review.mapper.ReviewMapper;
import ma.errabi.microservice.core.review.repository.ReviewRepository;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public Mono<Void> deleteReviews(int productId) {
        try {
            log.debug("Deleting all reviews for productId: {}", productId);
            return reviewRepository.deleteAll(reviewRepository.findByProductId(productId));
        } catch (Exception e) {
            log.error("Failed to delete reviews for productId: {}", productId, e);
            throw new EntityNotFoundException("Failed to delete reviews for productId: " + productId);
        }
    }
    public Mono<ReviewDTO> saveReview(ReviewDTO review) {
        return reviewRepository.save(reviewMapper.toEntity(review))
                .map(reviewMapper::toDTO);
    }
    public Flux<Page<ReviewDTO>> getAllReviews(int productId, Pageable pageable) {
       return reviewRepository.findByProductId(productId, pageable)
               .collectList()
                .map(reviewEntities -> new PageImpl<>(reviewEntities, pageable, reviewEntities.size()))
                .map(page -> page.map(reviewMapper::toDTO))
                .flux();
    }
    public Flux<ReviewDTO> getReview(String productId) {
        return reviewRepository.findByProductId(Integer.parseInt(productId))
                .map(reviewMapper::toDTO);
    }
}
