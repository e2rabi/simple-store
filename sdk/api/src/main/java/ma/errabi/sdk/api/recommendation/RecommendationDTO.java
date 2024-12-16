package ma.errabi.sdk.api.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationDTO {
    private String productId;
    private String recommendationId;
    private String author;
    private String content;
    private int rating;
}
