package lk.dimuthu.autohive.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(name = "seller_id", length = 36, nullable = false)
    private String sellerId;

    @Column(nullable = false)
    private String name;

    private String brand;

    @Column(nullable = false)
    private Double price;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer stock = 0;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
