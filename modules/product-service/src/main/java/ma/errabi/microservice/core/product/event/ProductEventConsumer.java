package ma.errabi.microservice.core.product.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.product.service.ProductService;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.event.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEventConsumer {
    private final ProductService productRepository;

    @Bean
    public Consumer<Event<String, ProductDTO>> deleteProductConsumer() {
        return message -> {
           log.info("Delete product with id : {}", message.getKey());
            productRepository.deleteProduct(message.getKey()).block();
        };
    }
}