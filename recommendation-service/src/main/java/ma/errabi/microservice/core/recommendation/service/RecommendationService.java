package ma.errabi.microservice.core.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.recommendation.domain.RecommendationEntity;
import ma.errabi.microservice.core.recommendation.mapper.RecommendationMapper;
import ma.errabi.microservice.core.recommendation.repository.RecommendationRepository;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository repository;
    private final RecommendationMapper mapper;

    public void deleteRecommendations(String productId) {
        log.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
        List<RecommendationEntity>  recommendationEntities  = repository.findByProductId(productId);
        if(recommendationEntities.isEmpty()) {
            log.debug("deleteRecommendations: nothing to delete");
           throw new IllegalArgumentException("No recommendations found for productId: " + productId);
        }
        repository.deleteAll(recommendationEntities);
    }
    public RecommendationDTO createRecommendation(RecommendationDTO dto) {
        log.debug("createRecommendation: tries to create recommendations for the product with productId: {}", dto.getProductId());
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }
  public List<RecommendationDTO> getRecommendations(String productId) {
        log.debug("getRecommendations: tries to get recommendations for the product with productId: {}", productId);
        return repository.findByProductId(productId).stream()
                .map(mapper::toDTO)
                .toList();
    }
}
