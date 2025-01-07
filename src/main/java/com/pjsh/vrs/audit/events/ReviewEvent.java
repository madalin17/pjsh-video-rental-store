package com.pjsh.vrs.audit.events;

public class ReviewEvent implements InteractionEvent {
    private String review;

    public ReviewEvent(Long customerId, Long videoId, String review) {
        super(customerId, videoId, "REVIEW");
        this.review = review;
    }

    public String getReview() {
        return review;
    }
}

