package com.pjsh.vrs.audit.events;

public class ReturnEvent {
    private final Long rentalId;

    public ReturnEvent(Long rentalId) {
        this.rentalId = rentalId;
    }

    public Long getRentalId() {
        return rentalId;
    }
}

