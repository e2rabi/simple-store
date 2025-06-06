package ma.errabi.microservice.core.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ProductConfig {
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    @Bean
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
    @Bean
    public WebClient webClient(@Qualifier("loadBalancedWebClientBuilder") WebClient.Builder builder) {
        return builder
                .filter(lbFunction)
                .build();
    }

}
