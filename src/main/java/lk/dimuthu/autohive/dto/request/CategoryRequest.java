package lk.dimuthu.autohive.dto.request;

public class CategoryRequest {
    private String name;
    private String parentId; // Only provide the parent category ID if this is a sub-category

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
}