package ma.errabi.microservice.composite.config;

import io.micrometer.tracing.Tracer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class CompositeConfig {

    @Value("${api.common.version}")         String apiVersion;
    @Value("${api.common.title}")           String apiTitle;
    @Value("${api.common.description}")     String apiDescription;
    @Value("${api.common.termsOfService}")  String apiTermsOfService;
    @Value("${api.common.license}")         String apiLicense;
    @Value("${api.common.licenseUrl}")      String apiLicenseUrl;
    @Value("${api.common.externalDocDesc}") String apiExternalDocDesc;
    @Value("${api.common.externalDocUrl}")  String apiExternalDocUrl;
    @Value("${api.common.contact.name}")    String apiContactName;
    @Value("${api.common.contact.url}")     String apiContactUrl;
    @Value("${api.common.contact.email}")   String apiContactEmail;


    @Bean
    @LoadBalanced
    public WebClient.Builder webClient() {
       return WebClient.builder();
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(Tracer tracer) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            if (tracer.currentSpan() != null) {
                var context = tracer.currentSpan().context();
                request.getHeaders().add("X-B3-TraceId", context.traceId());
                request.getHeaders().add("X-B3-SpanId", context.spanId());
                if (context.parentId() != null) {
                   request.getHeaders().add("X-B3-ParentSpanId", context.parentId());
                }
                request.getHeaders().add("X-B3-Sampled", context.sampled() != null ? context.sampled().toString() : "1");
            }
            return execution.execute(request, body);
        });
        return restTemplate;
    }

   @Bean
   public OpenAPI customOpenAPI() {
       return new OpenAPI().info(new Info()
               .title("My API")
               .version("1.0")
               .description("Demo API with WebFlux + Springdoc"));
   }
}
