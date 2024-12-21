package ma.errabi.microservice.core.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.product.domain.ProductEntity;
import ma.errabi.microservice.core.product.repository.ProductRepository;
import ma.errabi.microservice.core.product.mapper.ProductMapper;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.util.ServiceUtil;
import ma.errabi.sdk.util.exception.EntityNotFoundException;
import ma.errabi.sdk.util.exception.TechnicalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ServiceUtil serviceUtil;
    private final ProductMapper productMapper;

    @Transactional
    public Mono<ProductDTO> createProduct(ProductDTO productDTO) {
        try {
            ProductDTO createdProduct =   productMapper.toDTO(productRepository.save(productMapper.toEntity(productDTO)).block());
            return Mono.just(createdProduct);
        }catch (Exception ex){
            log.error("createProduct: Error creating the product: {}", ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        }
    }
    @Transactional(readOnly = true)
    public Mono<ProductDTO> getProductById(String productId) {
        return productRepository.findById(productId)
                .map(productMapper::toDTO)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No product found for productId: " + productId)));
    }
    @Transactional
    public void deleteProduct(String productId) {
       ProductEntity product = productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No product found for productId: " + productId))).block();
        assert product != null;
        productRepository.deleteById(product.getProductId()).block();
    }

    @Transactional(readOnly = true)
    public Mono<Page<ProductDTO>> getAllProducts(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return productRepository.findAllBy(pageRequest)
                .collectList()
                .zipWith(productRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()))
                .map(page -> page.map(productMapper::toDTO));
    }
}
