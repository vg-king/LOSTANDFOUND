package com.exampleKiitFinder.KittFinder.dto;

public class FoundRequest {
    private Long itemId;
    private String message;

    public FoundRequest(){}

    public FoundRequest(Long itemId, String message) {
        this.itemId = itemId;
        this.message = message;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
