package lk.dimuthu.autohive.dto.response;

public class VehicleResponse {
    private String id;
    private String userId;
    private String make;
    private String model;
    private Integer year;

    public VehicleResponse(String id, String userId, String make, String model, Integer year) {
        this.id = id;
        this.userId = userId;
        this.make = make;
        this.model = model;
        this.year = year;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public Integer getYear() { return year; }
}
