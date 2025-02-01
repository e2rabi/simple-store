package ma.errabi.microservice.core.recommendation.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.recommendation.domain.Recommendation;
import ma.errabi.microservice.core.recommendation.mapper.RecommendationMapper;
import ma.errabi.sdk.api.common.CustomPage;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationMapper mapper;
    private DynamoDbEnhancedClient enhancedClient ;
    private static final String recommendationsTableName = "Recommendation";
    @Value("${aws.dynamodb.endpoint}")
    private String dynamoDbHost;
    @Value("${aws.dynamodb.region}")
    private String region ;

    @PostConstruct
    private void init() {
        Region region =  Region.of(this.region);
        DynamoDbClient client =  DynamoDbClient.builder()
                .region(region)
                .endpointOverride(URI.create(dynamoDbHost))
                .build();
        enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();
    }
    public void deleteRecommendations(String id,String productId) {
        try{
            log.debug("Deleting recommendation with id: {}", productId);
            final DynamoDbTable<Recommendation> recommendationTable =
                    enhancedClient.table(recommendationsTableName, TableSchema.fromBean(Recommendation.class));
            Key key = Key.builder()
                    .partitionValue(id)
                    .sortValue(productId)
                    .build();
            recommendationTable.deleteItem(key);
        }catch (DynamoDbException ex) {
            log.error("error deleting recommendation with id: {}",productId,ex);
        }
    }
    public RecommendationDTO createRecommendation(RecommendationDTO item) {
        try {
            DynamoDbTable<Recommendation> workTable = enhancedClient.table("Recommendation", TableSchema.fromBean(Recommendation.class));
            String myGuid = java.util.UUID.randomUUID().toString();
            Recommendation record = new Recommendation();
            record.setProductId(item.getProductId());
            record.setId(myGuid);
            record.setRating(item.getRating());
            record.setAuthor(item.getAuthor());
            record.setContent(item.getContent());
            workTable.putItem(record);
            item.setId(myGuid);
            return item;
        } catch (DynamoDbException e) {
           log.error("An error occurred while creating recommendation: {}", e.getMessage());
        }
        return null;
    }
    public CustomPage<RecommendationDTO> scanByRecommendationByProductId(String productId) {
           DynamoDbTable<Recommendation> productCatalog = enhancedClient.table("Recommendation", TableSchema.fromBean(Recommendation.class));
           DynamoDbIndex<Recommendation> index = productCatalog.index("recommendation_by_author");

           Map<String, AttributeValue> expressionValues = Map.of(
                   ":productId", AttributeValue.builder().s(productId).build());
           ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                   .consistentRead(true)
                   .attributesToProject("id", "productId", "author", "rating", "content")
                   .filterExpression(Expression.builder()
                           .expression("productId = :productId")
                           .expressionValues(expressionValues)
                           .build())
                   .build();
           SdkIterable<Page<Recommendation>> pagedResults = index.scan(request);
           List<RecommendationDTO> recommendationDTOList = pagedResults.stream()
                   .flatMap(page -> page.items().stream())
                   .map(mapper::toDTO)
                   .collect(Collectors.toList());
           return new CustomPage<>(recommendationDTOList, recommendationDTOList.size());
    }
}
