package com.pjsh.vrs.audit.events;

public class RentalEvent {
    private Long customerId;

    private Long videoId;

    private String eventType; // "RENTAL", "RETURN"

    public RentalEvent(Long customerId, Long videoId, String eventType) {
        this.customerId = customerId;
        this.videoId = videoId;
        this.eventType = eventType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public String getEventType() {
        return eventType;
    }
}

