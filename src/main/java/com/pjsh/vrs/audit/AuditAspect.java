package com.pjsh.vrs.audit;

import com.pjsh.vrs.audit.events.RatingEvent;
import com.pjsh.vrs.audit.events.RentEvent;
import com.pjsh.vrs.audit.events.ReturnEvent;
import com.pjsh.vrs.audit.events.ReviewEvent;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Review;
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
        RentEvent event = new RentEvent(customerId, videoId);
        applicationContext.publishEvent(event);
    }

    @Before("anyReturnVideo()")
    public void logReturnAction(JoinPoint joinPoint) {
        Object[] methodArgs = joinPoint.getArgs();
        Long rentalId = (Long) methodArgs[0];
        ReturnEvent event = new ReturnEvent(rentalId);
        applicationContext.publishEvent(event);
    }

    @Before("anyAddReview()")
    public void logReviewAction(JoinPoint joinPoint) {
        Object[] methodArgs = joinPoint.getArgs();
        Review review = (Review) methodArgs[0];
        ReviewEvent event = new ReviewEvent(review.getCustomer().getId(), review.getVideo().getId(), review.getDescription());
        applicationContext.publishEvent(event);
    }

    @Before("anyAddRating()")
    public void logRatingAction(JoinPoint joinPoint) {
        Object[] methodArgs = joinPoint.getArgs();
        Rating rating = (Rating) methodArgs[0];
        RatingEvent event = new RatingEvent(rating.getCustomer().getId(), rating.getVideo().getId(), rating.getScore());
        applicationContext.publishEvent(event);
    }
}

