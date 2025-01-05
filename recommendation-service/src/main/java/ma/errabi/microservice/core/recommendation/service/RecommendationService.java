package ma.errabi.microservice.core.recommendation.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.recommendation.domain.Recommendation;
import ma.errabi.microservice.core.recommendation.mapper.RecommendationMapper;
import ma.errabi.sdk.api.common.CustomPage;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static software.amazon.awssdk.enhanced.dynamodb.internal.AttributeValues.numberValue;

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
    public List<RecommendationDTO> getAllRecommendations() {
        try {
            log.debug("Loading all recommendations");
            DynamoDbTable<Recommendation> table = enhancedClient.table(recommendationsTableName, TableSchema.fromBean(Recommendation.class));
            Iterator<Recommendation> results = table.scan().items().iterator();
            Recommendation workItem;
            ArrayList<Recommendation> itemList = new ArrayList<>();

            while (results.hasNext()) {
                workItem = new Recommendation();
                Recommendation work = results.next();
                workItem.setId(work.getId());
                workItem.setRating(work.getRating());
                workItem.setAuthor(work.getAuthor());
                workItem.setContent(work.getContent());
                workItem.setProductId(work.getProductId());
                itemList.add(workItem);
            }
            return itemList.stream()
                    .map(mapper::toDTO)
                    .toList() ;

        } catch (DynamoDbException e) {
           log.error("An error occurred while loading recommendations: {}", e.getMessage());
        }
        return null;
    }
    public void deleteRecommendations(String productId) {
        try{
            log.debug("Deleting recommendation with id: {}", productId);
            final DynamoDbTable<Recommendation> recommendationTable =
                    enhancedClient.table(recommendationsTableName, TableSchema.fromBean(Recommendation.class));
             recommendationTable.deleteItem(r -> r.key(Key.builder()
                    .partitionValue(productId)
                    .build()));
        }catch (DynamoDbException e) {
            log.error("error deleting recommendation with id: {}",productId);
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
    public RecommendationDTO getRecommendations(String id, String productId) {
        try {
            log.debug("Loading recommendation with id: {} and productId: {}", id, productId);
            final DynamoDbTable<Recommendation> recommendationTable =
                    enhancedClient.table("Recommendation", TableSchema.fromBean(Recommendation.class));
            Recommendation result = recommendationTable.getItem(r -> r.key(Key.builder()
                    .partitionValue(id)
                    .sortValue(productId)
                    .build()));
            return mapper.toDTO(result);
        } catch (DynamoDbException e) {
            log.error("Error loading recommendation with id: {} and productId: {}", id, productId, e);
        }
        return null;
    }
  public RecommendationDTO updateRecommendation(RecommendationDTO item) {
        try {
            log.debug("Updating recommendation with id: {}", item.getId());
            DynamoDbTable<Recommendation> workTable = enhancedClient.table("Recommendation", TableSchema.fromBean(Recommendation.class));
            Recommendation record = new Recommendation();
            record.setProductId(item.getProductId());
            record.setId(item.getId());
            record.setRating(item.getRating());
            record.setAuthor(item.getAuthor());
            record.setContent(item.getContent());
            workTable.putItem(record);
            return item;
        } catch (DynamoDbException e) {
            log.error("An error occurred while updating recommendation: {}", e.getMessage());
        }
        return null;
    }
    public CustomPage<RecommendationDTO> scanSyncRecommendationRating(Integer minRating, Integer maxRating) {
        DynamoDbTable<Recommendation> productCatalog = enhancedClient.table("Recommendation", TableSchema.fromBean(Recommendation.class));
        Map<String, AttributeValue> expressionValues = Map.of(
                ":min_rating", numberValue(minRating),
                ":max_rating", numberValue(maxRating));
        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .consistentRead(true)
                .attributesToProject("id", "productId", "author", "rating","content")
                .filterExpression(Expression.builder()
                        .expression("rating >= :min_rating AND rating <= :max_rating")
                        .expressionValues(expressionValues)
                        .build())
                .build();
        PageIterable<Recommendation> pagedResults = productCatalog.scan(request);
        List<RecommendationDTO> recommendationDTOList = pagedResults.stream()
                .flatMap(page -> page.items().stream())
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return new CustomPage<>(recommendationDTOList,pagedResults.items().stream().count());
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
        return new CustomPage<>(recommendationDTOList,pagedResults.stream().count());
    }
}
