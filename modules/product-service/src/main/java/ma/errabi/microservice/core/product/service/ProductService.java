package ma.errabi.microservice.core.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.product.repository.ProductRepository;
import ma.errabi.microservice.core.product.mapper.ProductMapper;
import ma.errabi.sdk.api.common.CustomPage;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.exception.EntityNotFoundException;
import ma.errabi.sdk.util.ServiceUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ServiceUtil  serviceUtil;

    public Mono<ProductDTO> createProduct(ProductDTO productDTO) {
        log.debug("Creating a new product with information: {}", productDTO);
        ProductDTO createdProduct =   productMapper.toDTO(productRepository.save(productMapper.toEntity(productDTO)).block());
        return Mono.just(createdProduct);
    }
    public Mono<ProductDTO> getProductById(String productId) {
        log.debug("Getting product with ID: {}", productId);
        log.info("Service address: {}", serviceUtil.getServiceAddress());
        return productRepository.findById(productId)
                .map(productMapper::toDTO)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No product found for productId: " + productId)));
    }
    public Mono<Void> deleteProduct(String productId) {
        log.debug("Deleting product with ID: {}", productId);
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No product found for productId: " + productId)))
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(product -> productRepository.delete(product).subscribe())
                .then();
    }
    public Mono<CustomPage<ProductDTO>> getAllProducts(int pageNumber, int pageSize) {
        log.debug("Getting all products");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return productRepository.findAllBy(pageRequest)
                .collectList()
                .zipWith(productRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()))
                .map(page -> new CustomPage<>(page.map(productMapper::toDTO)));
    }
}
