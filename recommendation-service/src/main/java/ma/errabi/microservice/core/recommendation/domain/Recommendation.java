package ma.errabi.microservice.core.recommendation.domain;



import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;


@Getter
@Setter
@DynamoDbBean
public class Recommendation {

    private String id;
    private String productId;
    private String author;
    private String content;
    private int rating;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
    @DynamoDbSortKey
    public String getProductId() {
        return productId;
    }
}
