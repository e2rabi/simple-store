package ma.errabi.sdk.api.review;

public class ReviewDTO {
    private int reviewId;
    private String name;
    private String description;

    public ReviewDTO() {
    }

    public ReviewDTO(int reviewId, String name, String description) {
        this.reviewId = reviewId;
        this.name = name;
        this.description = description;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
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
