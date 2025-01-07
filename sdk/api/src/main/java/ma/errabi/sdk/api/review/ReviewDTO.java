package ma.errabi.sdk.api.review;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private int id;
    private String productId;
    private String author;
    private String subject;
    private String content;
}
