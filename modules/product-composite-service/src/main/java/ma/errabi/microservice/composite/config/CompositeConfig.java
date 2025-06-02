package ma.errabi.microservice.composite.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
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
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

   @Bean
   public OpenAPI customOpenAPI() {
       return new OpenAPI().info(new Info()
               .title("My API")
               .version("1.0")
               .description("Demo API with WebFlux + Springdoc"));
   }
}
