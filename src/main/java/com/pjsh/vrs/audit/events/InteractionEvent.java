package com.pjsh.vrs.audit.events;

public class InteractionEvent {
    private Long customerId;

    private Long videoId;

    private String eventType; // "RATING", "REVIEW"

    public InteractionEvent(Long customerId, Long videoId, String eventType) {
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
