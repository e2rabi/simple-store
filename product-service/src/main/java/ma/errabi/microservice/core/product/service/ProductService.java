package ma.errabi.microservice.core.product.service;

import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.microservice.core.product.repository.ProductRepository;
import ma.errabi.microservice.core.product.mapper.ProductMapper;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.util.ServiceUtil;
import ma.errabi.sdk.util.exception.EntityNotFoundException;
import ma.errabi.sdk.util.exception.InvalidInputException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ServiceUtil serviceUtil;
    private final ProductMapper productMapper;


    public ProductDTO createProduct(ProductDTO productDTO) {
        try {
            return productMapper.toDTO(productRepository.save(productMapper.toEntity(productDTO)));
        }catch (DuplicateKeyException ex){
            log.error("Duplicate key for product id: {}",productDTO.getProductId());
            throw new InvalidInputException("Duplicate key for product id: "+productDTO.getProductId());
        }
    }
    public ProductDTO getProductById(Integer productId) {
        return productRepository.findByProductId(productId)
                .map(productMapper::toDTO)
                .map(productDTO -> {
                    productDTO.setServiceAddress(serviceUtil.getServiceAddress());
                    return productDTO;
                })
                .orElseThrow(() -> new EntityNotFoundException("No product found for productId: " + productId));
    }
    public void deleteProduct(Integer productId) {
        productRepository.findByProductId(productId)
                         .ifPresent(productRepository::delete);
    }
}
