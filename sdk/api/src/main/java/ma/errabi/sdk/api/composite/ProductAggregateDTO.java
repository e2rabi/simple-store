package ma.errabi.sdk.api.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.review.ReviewDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAggregateDTO {
    private String productId;
    private String name;
    private Double weight;
    private List<RecommendationDTO> recommendations ;
    private List<ReviewDTO> reviews;
    private String serviceAddress;


}
