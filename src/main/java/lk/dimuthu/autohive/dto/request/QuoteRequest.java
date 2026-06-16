package lk.dimuthu.autohive.dto.request;

public class QuoteRequest {
    private String inquiryId;
    private String sellerId;
    private Double price;
    private Integer deliveryTimeDays;

    // Getters and Setters
    public String getInquiryId() { return inquiryId; }
    public void setInquiryId(String inquiryId) { this.inquiryId = inquiryId; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getDeliveryTimeDays() { return deliveryTimeDays; }
    public void setDeliveryTimeDays(Integer deliveryTimeDays) { this.deliveryTimeDays = deliveryTimeDays; }
}