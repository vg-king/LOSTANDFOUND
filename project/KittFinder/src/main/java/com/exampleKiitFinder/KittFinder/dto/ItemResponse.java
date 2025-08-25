package com.exampleKiitFinder.KittFinder.dto;

public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private String location;
    private String category;
    private String imageUrl;
    private String createAt;
    private String updatedAt;
    private String createdAtFormatted; // User-friendly formatted date (e.g., "2 hours ago", "1 day ago")
    private Double reward;
    private String postedByName;
    private Long postedById;
    private boolean hasFoundMarkings; // New field to indicate if someone marked it as found
    private int foundMarkingsCount; // New field to show how many people marked it as found
    private String status; // LOST, FOUND_PENDING, FOUND_CONFIRMED

    public ItemResponse(){}

    public ItemResponse(Long id, String name, String description, String location, String category,
                        String imageUrl, String createAt, String updatedAt, String createdAtFormatted, Double reward,
                        String postedByName, Long postedById, boolean hasFoundMarkings, int foundMarkingsCount,
                        String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.category = category;
        this.imageUrl = imageUrl;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
        this.createdAtFormatted = createdAtFormatted;
        this.reward = reward;
        this.postedByName = postedByName;
        this.postedById = postedById;
        this.hasFoundMarkings = hasFoundMarkings;
        this.foundMarkingsCount = foundMarkingsCount;
        this.status = status;
    }

    // Constructor without found markings (for backward compatibility)
    public ItemResponse(Long id, String name, String description, String location, String category,
                        String imageUrl, String createAt, String updatedAt, Double reward,
                        String postedByName, Long postedById) {
        this(id, name, description, location, category, imageUrl, createAt, updatedAt, "Unknown", reward,
                postedByName, postedById, false, 0, "LOST");
    }

    // All existing getters and setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAtFormatted() {
        return createdAtFormatted;
    }

    public void setCreatedAtFormatted(String createdAtFormatted) {
        this.createdAtFormatted = createdAtFormatted;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    public Long getPostedById() {
        return postedById;
    }

    public void setPostedById(Long postedById) {
        this.postedById = postedById;
    }

    // New getters and setters for found markings
    public boolean isHasFoundMarkings() {
        return hasFoundMarkings;
    }

    public void setHasFoundMarkings(boolean hasFoundMarkings) {
        this.hasFoundMarkings = hasFoundMarkings;
    }

    public int getFoundMarkingsCount() {
        return foundMarkingsCount;
    }

    public void setFoundMarkingsCount(int foundMarkingsCount) {
        this.foundMarkingsCount = foundMarkingsCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}