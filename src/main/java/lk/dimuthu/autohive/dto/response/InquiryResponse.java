package lk.dimuthu.autohive.dto.response;

import java.time.LocalDateTime;

public class InquiryResponse {
    private String id;
    private String userId;
    private String userName;
    private String vehicleInfo; // උදා: "Toyota Corolla (2018)"
    private String categoryName;
    private String inquiryType;
    private String partDescription;
    private String imageUrl;
    private String status;
    private LocalDateTime createdAt;

    public InquiryResponse(String id, String userId, String userName, String vehicleInfo,
                           String categoryName, String inquiryType, String partDescription,
                           String imageUrl, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.vehicleInfo = vehicleInfo;
        this.categoryName = categoryName;
        this.inquiryType = inquiryType;
        this.partDescription = partDescription;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getVehicleInfo() { return vehicleInfo; }
    public String getCategoryName() { return categoryName; }
    public String getInquiryType() { return inquiryType; }
    public String getPartDescription() { return partDescription; }
    public String getImageUrl() { return imageUrl; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
