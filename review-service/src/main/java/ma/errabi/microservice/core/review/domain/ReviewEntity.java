package ma.errabi.microservice.core.review.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reviews",indexes = {@Index(name = "reviews_unique_idx",unique = true,columnList = "productId,reviewId")})
public class ReviewEntity {
    @Id
    @GeneratedValue
    private int id;
    private int reviewId;
    private int productId;
    private String author;
    private String subject;
    private String content;
    @Version
    private int version;
}
