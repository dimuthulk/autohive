package lk.dimuthu.autohive.dto.request;

public class OrderStatusUpdateRequest {
    private String sellerId;
    private String status; // 'shipped' හෝ 'delivered'

    // Getters and Setters
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}