package ma.errabi.microservice.core.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.recommendation.domain.Recommendation;
import ma.errabi.microservice.core.recommendation.mapper.RecommendationMapper;
import ma.errabi.microservice.core.recommendation.repository.RecommendationRepository;
import ma.errabi.sdk.api.common.CustomPage;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.exception.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationMapper mapper;
    private final RecommendationRepository recommendationRepository ;

    @Transactional
    public void deleteRecommendations(String productId) {
        log.info("Delete recommendation by product id {}",productId);
        var recommendation =  recommendationRepository.findByProductId(productId);
        if (recommendation.isEmpty()) {
            log.warn("No recommendation found for product id {}",productId);
            throw new EntityNotFoundException("Recommendation not found");
        }
        recommendationRepository.delete(recommendation.get());
    }
    public RecommendationDTO createRecommendation(RecommendationDTO item) {
        Recommendation newItem = recommendationRepository.save(mapper.toEntity(item));
        return mapper.toDTO(newItem);
    }
    public CustomPage<RecommendationDTO> scanByRecommendationByProductId(String productId, Pageable page) {
           var pagedResults = recommendationRepository.findByProductId(productId,page);
           List<RecommendationDTO> recommendationDTOList = pagedResults.stream()
                   .map(mapper::toDTO)
                   .collect(Collectors.toList());
           return new CustomPage<>(recommendationDTOList, recommendationDTOList.size());
    }
}
