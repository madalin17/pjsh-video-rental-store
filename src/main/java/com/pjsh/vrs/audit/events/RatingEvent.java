package com.pjsh.vrs.audit.events;

public class RatingEvent extends InteractionEvent {
    private Double score;

    public RatingEvent(Long customerId, Long videoId, Double score) {
        super(customerId, videoId, "RATING");
        this.score = score;
    }

    public Double getScore() {
        return score;
    }
}
