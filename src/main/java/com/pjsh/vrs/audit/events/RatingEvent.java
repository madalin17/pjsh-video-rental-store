package com.pjsh.vrs.audit.events;

public class RatingEvent extends InteractionEvent {
    private Integer score;

    public RatingEvent(Long customerId, Long videoId, Integer score) {
        super(customerId, videoId, "RATING");
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }
}
