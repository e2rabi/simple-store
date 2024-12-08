package ma.errabi.microservice.core.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.product.repository.ProductRepository;
import ma.errabi.microservice.core.product.mapper.ProductMapper;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.util.ServiceUtil;
import ma.errabi.sdk.util.exception.EntityNotFoundException;
import ma.errabi.sdk.util.exception.TechnicalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ServiceUtil serviceUtil;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        try {
            ProductDTO createdProduct =   productMapper.toDTO(productRepository.save(productMapper.toEntity(productDTO)));
            createdProduct.setServiceAddress(serviceUtil.getServiceAddress());
            return createdProduct;
        }catch (Exception ex){
            log.error("createProduct: Error creating the product: {}", ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        }
    }
    @Transactional(readOnly = true)
    public ProductDTO getProductById(String productId) {
        return productRepository.findById(productId)
                .map(productMapper::toDTO)
                .map(productDTO -> {
                    productDTO.setServiceAddress(serviceUtil.getServiceAddress());
                    return productDTO;
                })
                .orElseThrow(() -> new EntityNotFoundException("No product found for productId: " + productId));
    }
    @Transactional
    public void deleteProduct(String productId) {
        productRepository.findById(productId)
                         .ifPresent(productRepository::delete);
    }
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(int pageNumber, int pageSize) {
        return productRepository.findAll(PageRequest.of(pageNumber, pageSize))
                                .map(productMapper::toDTO);
    }
}
