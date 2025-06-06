package ma.errabi.microservice.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class GatewayConfig {

   /* @Autowired
    private ReactorLoadBalancerExchangeFilterFunction lbFunction;
    @Bean
    public WebClient getLoadBalanceClient(WebClient.Builder builder) {
        return builder.filter(lbFunction).build();
    }*/

}
