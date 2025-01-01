package ma.errabi.microservice.core.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@ComponentScan(basePackages = "ma.errabi")
public class RecommendationServiceApplication implements ApplicationRunner {
	private final ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(RecommendationServiceApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
				.endpointOverride(URI.create("http://localhost:8000"))
				.build();

		CreateTableRequest request = CreateTableRequest.builder()
				.tableName("Recommendation")
				.keySchema(KeySchemaElement.builder()
						.attributeName("id")
						.keyType(KeyType.HASH)
						.build())
				.attributeDefinitions(AttributeDefinition.builder()
						.attributeName("id")
						.attributeType(ScalarAttributeType.S)
						.build())
				.provisionedThroughput(ProvisionedThroughput.builder()
						.readCapacityUnits(5L)
						.writeCapacityUnits(5L)
						.build())
				.build();

	//	dynamoDbClient.createTable(request);
		System.out.println("Table created successfully!");
		String mongoDbHost = context.getEnvironment().getProperty("aws.dynamodb.endpoint");
		log.info("Connected to dynamodb : {}",mongoDbHost);
	}
}
