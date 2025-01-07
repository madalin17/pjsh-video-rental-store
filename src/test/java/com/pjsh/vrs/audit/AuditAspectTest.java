package com.pjsh.vrs.audit;

import static org.mockito.Mockito.*;

import com.pjsh.vrs.audit.events.RentalEvent;
import com.pjsh.vrs.service.RentalService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationContext;

class AuditAspectTest {

    @InjectMocks
    private AuditAspect auditAspect;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogRentAction() {
        Long customerId = 1L;
        Long videoId = 1L;

        // Mock the JoinPoint to simulate the rentVideo method call
        JoinPoint joinPoint = mock(JoinPoint.class);
        when(joinPoint.getArgs()).thenReturn(new Object[] { customerId, videoId });

        // Call the logRentAction method to trigger the event
        auditAspect.logRentAction(joinPoint);

        // Verify that the event is published
        verify(applicationContext).publishEvent(any(RentalEvent.class));
    }

    @Test
    void testLogReturnAction() {
        Long customerId = 1L;
        Long videoId = 1L;

        JoinPoint joinPoint = mock(JoinPoint.class);
        when(joinPoint.getArgs()).thenReturn(new Object[] { customerId, videoId });

        auditAspect.logReturnAction(joinPoint);

        verify(applicationContext).publishEvent(any(RentalEvent.class));
    }
}
