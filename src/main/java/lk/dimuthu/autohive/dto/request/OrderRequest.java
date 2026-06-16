package lk.dimuthu.autohive.dto.request;

public class OrderRequest {
    private String quoteId;
    private String buyerId;

    // Getters and Setters
    public String getQuoteId() { return quoteId; }
    public void setQuoteId(String quoteId) { this.quoteId = quoteId; }
    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
}
