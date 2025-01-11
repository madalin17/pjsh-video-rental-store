package com.pjsh.vrs.mock.service;

import static org.mockito.Mockito.*;

import com.pjsh.vrs.audit.events.RentEvent;
import com.pjsh.vrs.audit.events.ReturnEvent;
import com.pjsh.vrs.audit.events.ReviewEvent;
import com.pjsh.vrs.audit.events.RatingEvent;
import com.pjsh.vrs.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @InjectMocks
    private AuditService auditService;

    @Mock
    private RentEvent rentEvent;

    @Mock
    private ReturnEvent returnEvent;

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
        when(rentEvent.getCustomerId()).thenReturn(1L);
        when(rentEvent.getVideoId()).thenReturn(1L);

        auditService.auditRent(rentEvent);

        verify(rentEvent).getCustomerId();
        verify(rentEvent).getVideoId();
    }

    @Test
    void testAuditReturn() {
        when(returnEvent.getRentalId()).thenReturn(1L);

        auditService.auditReturn(returnEvent);

        verify(returnEvent).getRentalId();
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
        when(ratingEvent.getScore()).thenReturn(4);

        auditService.auditRating(ratingEvent);

        verify(ratingEvent).getCustomerId();
        verify(ratingEvent).getVideoId();
        verify(ratingEvent).getScore();
    }
}
