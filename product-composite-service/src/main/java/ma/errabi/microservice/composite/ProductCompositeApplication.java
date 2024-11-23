package ma.errabi.microservice.composite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ma.errabi")
public class ProductCompositeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeApplication.class, args);
	}

}
