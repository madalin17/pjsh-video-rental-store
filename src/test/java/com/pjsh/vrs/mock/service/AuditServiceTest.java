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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
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

    @Value("${rating1.score}")
    private Integer rating1Score;
    @Value("${review1.description}")
    private String review1Description;

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
        when(reviewEvent.getReview()).thenReturn(review1Description);

        auditService.auditReview(reviewEvent);

        verify(reviewEvent).getCustomerId();
        verify(reviewEvent).getVideoId();
        verify(reviewEvent).getReview();
    }

    @Test
    void testAuditRating() {
        when(ratingEvent.getCustomerId()).thenReturn(1L);
        when(ratingEvent.getVideoId()).thenReturn(1L);
        when(ratingEvent.getScore()).thenReturn(rating1Score);

        auditService.auditRating(ratingEvent);

        verify(ratingEvent).getCustomerId();
        verify(ratingEvent).getVideoId();
        verify(ratingEvent).getScore();
    }
}
