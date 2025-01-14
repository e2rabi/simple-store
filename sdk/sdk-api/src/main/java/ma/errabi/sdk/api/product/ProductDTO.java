package ma.errabi.sdk.api.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String productId;
    private String name;
    private String description;
    private double weight;
    private String serviceAddress;
}
