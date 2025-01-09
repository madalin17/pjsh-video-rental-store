package com.pjsh.vrs.service;

import static org.mockito.Mockito.*;

import com.pjsh.vrs.audit.events.RentalEvent;
import com.pjsh.vrs.audit.events.ReviewEvent;
import com.pjsh.vrs.audit.events.RatingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @InjectMocks
    private AuditService auditService;

    @Mock
    private RentalEvent rentalEvent;

    @Mock
    private ReviewEvent reviewEvent;

    @Mock
    private RatingEvent ratingEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuditRent() {
        when(rentalEvent.getCustomerId()).thenReturn(1L);
        when(rentalEvent.getVideoId()).thenReturn(1L);
        when(rentalEvent.getEventType()).thenReturn("RENTAL");

        auditService.auditRent(rentalEvent);

        verify(rentalEvent).getCustomerId();
        verify(rentalEvent).getVideoId();
        verify(rentalEvent).getEventType();
    }

    @Test
    void testAuditReview() {
        when(reviewEvent.getCustomerId()).thenReturn(1L);
        when(reviewEvent.getVideoId()).thenReturn(1L);
        when(reviewEvent.getReview()).thenReturn("Great video!");

        auditService.auditReview(reviewEvent);

        verify(reviewEvent).getCustomerId();
        verify(reviewEvent).getVideoId();
        verify(reviewEvent).getReview();
    }

    @Test
    void testAuditRating() {
        when(ratingEvent.getCustomerId()).thenReturn(1L);
        when(ratingEvent.getVideoId()).thenReturn(1L);
        when(ratingEvent.getScore()).thenReturn(4.5);

        auditService.auditRating(ratingEvent);

        verify(ratingEvent).getCustomerId();
        verify(ratingEvent).getVideoId();
        verify(ratingEvent).getScore();
    }
}
