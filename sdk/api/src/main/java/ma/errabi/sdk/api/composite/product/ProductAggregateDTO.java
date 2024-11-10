package ma.errabi.sdk.api.composite.product;

import java.util.List;

public class ProductAggregateDTO {
    private Integer productId;
    private String name;
    private Integer weight;
    private List<RecommendationSummaryDTO> recommendations ;
    private List<ReviewSummaryDTO> reviews;
    private ServiceAddressDTO serviceAddress;
}
