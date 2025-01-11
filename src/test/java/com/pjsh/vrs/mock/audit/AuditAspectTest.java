package com.pjsh.vrs.mock.audit;

import static org.mockito.Mockito.*;

import com.pjsh.vrs.audit.AuditAspect;
import com.pjsh.vrs.audit.events.RentEvent;
import com.pjsh.vrs.audit.events.ReturnEvent;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuditAspectTest {

    @InjectMocks
    private AuditAspect auditAspect;

    @Mock
    private ApplicationContext applicationContext;

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

        verify(applicationContext).publishEvent(any(RentEvent.class));
    }

    @Test
    void testLogReturnAction() {
        Long rentalId = 1L;

        JoinPoint joinPoint = mock(JoinPoint.class);
        when(joinPoint.getArgs()).thenReturn(new Object[] { rentalId });

        auditAspect.logReturnAction(joinPoint);

        verify(applicationContext).publishEvent(any(ReturnEvent.class));
    }
}
