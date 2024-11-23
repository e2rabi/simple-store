package ma.errabi.sdk.api.recommendation;

public class RecommendationDTO {
    private int recommendationId;
    private String name;
    private String description;

    public RecommendationDTO() {
    }

    public RecommendationDTO(int recommendationId, String name, String description) {
        this.recommendationId = recommendationId;
        this.name = name;
        this.description = description;
    }

    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
