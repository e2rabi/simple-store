package ma.errabi.microservice.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class GatewayConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder getLoadBalanceClient() {
      return WebClient.builder();
    }
}
