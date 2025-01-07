package com.pjsh.vrs.audit;

import com.pjsh.vrs.audit.events.RatingEvent;
import com.pjsh.vrs.audit.events.RentalEvent;
import com.pjsh.vrs.audit.events.ReviewEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(* com.pjsh.vrs.service.RentalService.rentVideo(..))")
    public void anyRentVideo() {}

    @Pointcut("execution(* com.pjsh.vrs.service.RentalService.returnVideo(..))")
    public void anyReturnVideo() {}

    @Pointcut("execution(* com.pjsh.vrs.service.ReviewService.addReview(..))")
    public void anyAddReview() {}

    @Pointcut("execution(* com.pjsh.vrs.service.RatingService.addRating(..))")
    public void anyAddRating() {}

    @Before("anyRentVideo()")
    public void logRentAction(JoinPoint joinPoint) {
        Object[] methodArgs = joinPoint.getArgs();
        Long customerId = (Long) methodArgs[0];
        Long videoId = (Long) methodArgs[1];
        RentalEvent event = new RentalEvent(customerId, videoId, "RENTAL");
        applicationContext.publishEvent(event);
    }

    @Before("anyReturnVideo()")
    public void logReturnAction(JoinPoint joinPoint) {
        Object[] methodArgs = joinPoint.getArgs();
        Long customerId = (Long) methodArgs[0];
        Long videoId = (Long) methodArgs[1];
        RentalEvent event = new RentalEvent(customerId, videoId, "RETURN");
        applicationContext.publishEvent(event);
    }

    @Before("anyAddReview()")
    public void logReviewAction(JoinPoint joinPoint) {
        Object[] methodArgs = joinPoint.getArgs();
        Long customerId = (Long) methodArgs[0];
        Long videoId = (Long) methodArgs[1];
        String review = (String) methodArgs[2];
        ReviewEvent event = new ReviewEvent(customerId, videoId, review);
        applicationContext.publishEvent(event);
    }

    @Before("anyAddRating()")
    public void logRatingAction(JoinPoint joinPoint) {
        Object[] methodArgs = joinPoint.getArgs();
        Long customerId = (Long) methodArgs[0];
        Long videoId = (Long) methodArgs[1];
        Double score = (Double) methodArgs[2];
        RatingEvent event = new RatingEvent(customerId, videoId, score);
        applicationContext.publishEvent(event);
    }
}

