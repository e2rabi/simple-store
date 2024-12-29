package ma.errabi.microservice.core.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.recommendation.domain.Recommendation;
import ma.errabi.microservice.core.recommendation.mapper.RecommendationMapper;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationMapper mapper;

    private DynamoDbClient getClient() {
        Region region = Region.US_EAST_1;
        return DynamoDbClient.builder()
                .region(region)
                .endpointOverride(URI.create("http://localhost:8000"))
                .build();
    }

    public List<RecommendationDTO> getAllRecommendations() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getClient())
                .build();

        try {
            DynamoDbTable<Recommendation> table = enhancedClient.table("Recommendation", TableSchema.fromBean(Recommendation.class));
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

                // Push the workItem to the list.
                itemList.add(workItem);
            }
            return itemList.stream().map(mapper::toDTO).toList() ;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }
    public void deleteRecommendations(String productId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    public RecommendationDTO createRecommendation(RecommendationDTO item) {
        try {
            DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(getClient())
                    .build();

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
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }
  public RecommendationDTO getRecommendations(String  id) {
     throw new UnsupportedOperationException("Not implemented yet");
  }
}
