package ma.errabi.microservice.core.recommendation.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.recommendation.repository.RecommendationRepository;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.event.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationEventConsumer {
    private final RecommendationRepository recommendationRepository;

    @Bean
    public Consumer<Event<String, RecommendationDTO>> deleteRecommendationConsumer() {
        return message -> {
            log.info("Delete recommendation with product id : {}", message.getKey());
            recommendationRepository.deleteByProductId(message.getKey());
        };
    }
}