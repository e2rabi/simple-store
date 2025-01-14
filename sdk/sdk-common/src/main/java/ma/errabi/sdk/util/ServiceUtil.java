package ma.errabi.sdk.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import ma.errabi.sdk.api.composite.ProductAggregateDTO;
import ma.errabi.sdk.api.product.ProductDTO;
import ma.errabi.sdk.api.recommendation.RecommendationDTO;
import ma.errabi.sdk.api.review.ReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceUtil.class);

    private final String port;

    private String serviceAddress = null;

    @Autowired
    public ServiceUtil(@Value("${server.port}") String port) {

        this.port = port;
    }

    public String getServiceAddress() {
        if (serviceAddress == null) {
            serviceAddress = findMyHostname() + "/" + findMyIpAddress() + ":" + port;
        }
        return serviceAddress;
    }

    private String findMyHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown host name";
        }
    }

    private String findMyIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown IP address";
        }
    }

    public ProductAggregateDTO createProductAggregate(ProductDTO productDTO, List<RecommendationDTO> recommendationDTOS, List<ReviewDTO> reviewDTOS) {
        ProductAggregateDTO productAggregateDTO =   new ProductAggregateDTO();
        productAggregateDTO.setProductId(productDTO.getProductId());
        productAggregateDTO.setName(productDTO.getName());
        productAggregateDTO.setWeight(productDTO.getWeight());
        productAggregateDTO.setRecommendations(recommendationDTOS);
        productAggregateDTO.setReviews(reviewDTOS);
        productAggregateDTO.setServiceAddress(getServiceAddress());
        return productAggregateDTO;
    }
}