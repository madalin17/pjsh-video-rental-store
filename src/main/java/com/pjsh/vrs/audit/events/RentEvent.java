package com.pjsh.vrs.audit.events;

public class RentEvent {
    private final Long customerId;

    private final Long videoId;

    public RentEvent(Long customerId, Long videoId) {
        this.customerId = customerId;
        this.videoId = videoId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getVideoId() {
        return videoId;
    }
}

