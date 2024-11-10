package ma.errabi.microservice.composite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CompositeConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
