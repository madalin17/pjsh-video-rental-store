package com.pjsh.vrs.service;

import com.pjsh.vrs.audit.events.RatingEvent;
import com.pjsh.vrs.audit.events.RentEvent;
import com.pjsh.vrs.audit.events.ReturnEvent;
import com.pjsh.vrs.audit.events.ReviewEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @EventListener
    public void auditRent(RentEvent event) {
        System.out.println("Rental Event: Customer " + event.getCustomerId() + " rented video " + event.getVideoId() + " - Event Type: RENT");
    }

    @EventListener
    public void auditReturn(ReturnEvent event) {
        System.out.println("Rental Event: Rental " + event.getRentalId() + " - Event Type: RETURN");
    }

    @EventListener
    public void auditReview(ReviewEvent event) {
        System.out.println("Review Event: Customer " + event.getCustomerId() + " reviewed video " + event.getVideoId() + " - Review: " + event.getReview());
    }

    @EventListener
    public void auditRating(RatingEvent event) {
        System.out.println("Rating Event: Customer " + event.getCustomerId() + " rated video " + event.getVideoId() + " - Rating: " + event.getScore());
    }
}

