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

    private Long video1Id, customer1Id, rating1Id, review1Id, rent1Id, return1Id;

    @Value("${rating1.score}")
    private Integer rating1Score;
    @Value("${review1.description}")
    private String review1Description;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        video1Id = 1L;
        customer1Id = 1L;
        rating1Id = 1L;
        review1Id = 1L;
        rent1Id = 1L;
        return1Id = 1L;
    }

    @Test
    void testAuditRent() {
        when(rentEvent.getCustomerId()).thenReturn(customer1Id);
        when(rentEvent.getVideoId()).thenReturn(video1Id);

        auditService.auditRent(rentEvent);

        verify(rentEvent).getCustomerId();
        verify(rentEvent).getVideoId();
    }

    @Test
    void testAuditReturn() {
        when(returnEvent.getRentalId()).thenReturn(rent1Id);

        auditService.auditReturn(returnEvent);

        verify(returnEvent).getRentalId();
    }

    @Test
    void testAuditReview() {
        when(reviewEvent.getCustomerId()).thenReturn(customer1Id);
        when(reviewEvent.getVideoId()).thenReturn(video1Id);
        when(reviewEvent.getReview()).thenReturn(review1Description);

        auditService.auditReview(reviewEvent);

        verify(reviewEvent).getCustomerId();
        verify(reviewEvent).getVideoId();
        verify(reviewEvent).getReview();
    }

    @Test
    void testAuditRating() {
        when(ratingEvent.getCustomerId()).thenReturn(customer1Id);
        when(ratingEvent.getVideoId()).thenReturn(video1Id);
        when(ratingEvent.getScore()).thenReturn(rating1Score);

        auditService.auditRating(ratingEvent);

        verify(ratingEvent).getCustomerId();
        verify(ratingEvent).getVideoId();
        verify(ratingEvent).getScore();
    }
}
