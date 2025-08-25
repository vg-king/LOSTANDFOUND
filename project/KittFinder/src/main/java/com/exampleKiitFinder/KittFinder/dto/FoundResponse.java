package com.exampleKiitFinder.KittFinder.dto;

public class FoundResponse {
    private Long id;
    private Long itemId;
    private String itemTitle;
    private String finderName;
    private String ownerName;
    private Long finderId;
    private Long ownerId;
    private boolean finderConfirmed;
    private boolean ownerConfirmed;
    private String finderConfirmedAt;
    private String ownerConfirmedAt;
    private String createdAt;
    private String finderMessage;
    private boolean bothConfirmed;

    public FoundResponse() {}

    public FoundResponse(Long id, Long itemId, String itemTitle, String finderName, String ownerName, Long finderId, Long ownerId, boolean finderConfirmed, boolean ownerConfirmed, String finderConfirmedAt, String ownerConfirmedAt, String createdAt, String finderMessage, boolean bothConfirmed) {
        this.id = id;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.finderName = finderName;
        this.ownerName = ownerName;
        this.finderId = finderId;
        this.ownerId = ownerId;
        this.finderConfirmed = finderConfirmed;
        this.ownerConfirmed = ownerConfirmed;
        this.finderConfirmedAt = finderConfirmedAt;
        this.ownerConfirmedAt = ownerConfirmedAt;
        this.createdAt = createdAt;
        this.finderMessage = finderMessage;
        this.bothConfirmed = bothConfirmed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getFinderName() {
        return finderName;
    }

    public void setFinderName(String finderName) {
        this.finderName = finderName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getFinderId() {
        return finderId;
    }

    public void setFinderId(Long finderId) {
        this.finderId = finderId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isFinderConfirmed() {
        return finderConfirmed;
    }

    public void setFinderConfirmed(boolean finderConfirmed) {
        this.finderConfirmed = finderConfirmed;
    }

    public boolean isOwnerConfirmed() {
        return ownerConfirmed;
    }

    public void setOwnerConfirmed(boolean ownerConfirmed) {
        this.ownerConfirmed = ownerConfirmed;
    }

    public String getFinderConfirmedAt() {
        return finderConfirmedAt;
    }

    public void setFinderConfirmedAt(String finderConfirmedAt) {
        this.finderConfirmedAt = finderConfirmedAt;
    }

    public String getOwnerConfirmedAt() {
        return ownerConfirmedAt;
    }

    public void setOwnerConfirmedAt(String ownerConfirmedAt) {
        this.ownerConfirmedAt = ownerConfirmedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFinderMessage() {
        return finderMessage;
    }

    public void setFinderMessage(String finderMessage) {
        this.finderMessage = finderMessage;
    }

    public boolean isBothConfirmed() {
        return bothConfirmed;
    }

    public void setBothConfirmed(boolean bothConfirmed) {
        this.bothConfirmed = bothConfirmed;
    }
}
