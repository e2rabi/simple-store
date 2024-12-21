package ma.errabi.microservice.core.review.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

@Data
@Table("reviews")
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {
    @Id
    private Integer id;
    private int productId;
    private String author;
    private String subject;
    private String content;

}
