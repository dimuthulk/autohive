package lk.dimuthu.autohive.dto.response;

public class ProductResponse {
    private String id;
    private String sellerId;
    private String sellerBusinessName;
    private String categoryId;
    private String categoryName;
    private String name;
    private String brand;
    private Double price;
    private Integer stock;
    private String imageUrl;

    public ProductResponse(String id, String sellerId, String sellerBusinessName,
                           String categoryId, String categoryName, String name,
                           String brand, Double price, Integer stock, String imageUrl) {
        this.id = id;
        this.sellerId = sellerId;
        this.sellerBusinessName = sellerBusinessName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getId() { return id; }
    public String getSellerId() { return sellerId; }
    public String getSellerBusinessName() { return sellerBusinessName; }
    public String getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public Double getPrice() { return price; }
    public Integer getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
}