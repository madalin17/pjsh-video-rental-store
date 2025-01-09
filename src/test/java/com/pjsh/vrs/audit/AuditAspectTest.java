package com.pjsh.vrs.audit;

import static org.mockito.Mockito.*;

import com.pjsh.vrs.audit.events.RentalEvent;
import com.pjsh.vrs.service.RentalService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
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

        JoinPoint joinPoint = mock(JoinPoint.class);
        when(joinPoint.getArgs()).thenReturn(new Object[] { customerId, videoId });

        auditAspect.logRentAction(joinPoint);

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
