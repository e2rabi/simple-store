package ma.errabi.microservice.core.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.review.repository.ReviewRepository;
import ma.errabi.sdk.api.review.ReviewDTO;
import ma.errabi.sdk.event.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewConsumerService {

    private final ReviewRepository reviewRepository;
    @Bean
    public Consumer<Event<String, ReviewDTO>> deleteReviewConsumer() {
        return message -> {
            log.info("Delete review with product id : {}", message.getKey());
            reviewRepository.deleteByProductId(message.getKey());
        };
    }
}
