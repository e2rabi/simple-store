package ma.errabi.sdk.api.product;

public class ProductDTO {
    private int productId;
    private String name;
    private String description;
    private int weight;
    private String serviceAddress;

    public ProductDTO() {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public ProductDTO(int productId, String name, String description, int weight, String serviceAddress) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.serviceAddress = serviceAddress;
    }
}
