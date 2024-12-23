package ma.errabi.microservice.core.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.recommendation.domain.Recommendation;
import ma.errabi.microservice.core.recommendation.mapper.RecommendationMapper;
import ma.errabi.microservice.core.recommendation.repository.RecommendationRepository;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository repository;
    private final RecommendationMapper mapper;

    public Mono<Void> deleteRecommendations(String productId) {
        log.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
        Flux<Recommendation> recommendationEntities  = repository.findByProductId(productId);
        return repository.deleteAll(recommendationEntities);
    }
    public Mono<RecommendationDTO> createRecommendation(RecommendationDTO dto) {
        log.debug("createRecommendation: tries to create recommendations for the product with productId: {}", dto.getProductId());
        Mono<Recommendation> recommendation =  repository.save(mapper.toEntity(dto));
        return recommendation.map(mapper::toDTO);
    }
  public Flux<RecommendationDTO> getRecommendations(String productId) {
        log.debug("getRecommendations: tries to get recommendations for the product with productId: {}", productId);
        return repository.findByProductId(productId)
                .map(mapper::toDTO);
    }
}
