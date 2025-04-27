package ma.errabi.microservice.core.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class ProductServiceApplication implements ApplicationRunner {

	private final ApplicationContext context;

	public static void main(String[] args) {
		 SpringApplication.run(ProductServiceApplication.class, args);
	}
	@Override
	public void run(ApplicationArguments args) {
		String mongoDbHost = context.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongoDbPort= context.getEnvironment().getProperty("spring.data.mongodb.port");
		log.info("Connected to MongoDb : {}-{}",mongoDbHost,mongoDbPort);
	}
}
