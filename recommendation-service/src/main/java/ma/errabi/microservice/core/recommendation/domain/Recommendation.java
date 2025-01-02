package ma.errabi.microservice.core.recommendation.domain;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;


@Getter
@Setter
@ToString
@DynamoDbBean
public class Recommendation {

    private String id;
    private String productId;

    private String author;
    private String content;
    private Integer rating;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
    @DynamoDbSortKey
    public String getProductId() {
        return productId;
    }
    @DynamoDbSecondaryPartitionKey(indexNames = "recommendation_by_author")
    public String getAuthor() {
        return author;
    }
}
