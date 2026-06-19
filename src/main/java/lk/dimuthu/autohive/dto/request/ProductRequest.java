package lk.dimuthu.autohive.dto.request;

public class ProductRequest {
    private String sellerId;
    private String categoryId;
    private String name;
    private String brand;
    private Double price;
    private Integer stock;

    // අලුතින් එකතු කළ කොටස
    private String imageUrl;

    // පරණ Getters and Setters ටික...
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    // අලුතින් එකතු කළ Getter සහ Setter
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}