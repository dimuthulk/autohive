package lk.dimuthu.autohive.dto.response;

public class CategoryResponse {
    private String id;
    private String name;
    private String parentId;
    private String parentName;

    public CategoryResponse(String id, String name, String parentId, String parentName) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.parentName = parentName;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getParentId() { return parentId; }
    public String getParentName() { return parentName; }
}