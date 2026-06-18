package lk.dimuthu.autohive.dto.request;

public class RatingRequest {
    private String orderId;
    private Integer ratingValue;
    private String review;

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Integer getRatingValue() { return ratingValue; }
    public void setRatingValue(Integer ratingValue) { this.ratingValue = ratingValue; }
    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }
}