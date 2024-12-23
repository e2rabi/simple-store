package ma.errabi.microservice.core.recommendation.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "recommendations")
@CompoundIndex(name = "prod-rec-id",unique = true,def = "{'productId':1,'recommendationId':1}")
public class Recommendation {
    @Id
    private String recommendationId;
    private String productId;
    private String author;
    private String content;
    private int rating;
    @Version
    private int version;
}
