package com.dealership.car.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@Slf4j
@Aspect
public class LoggerAspect {
    @Around("execution(* com.dealership.car..*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info(joinPoint.getSignature().toString() + "Method execution start");
        Instant start =Instant.now();
        Object returtObject = joinPoint.proceed();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start,finish).toMillis();
        log.info("Time took to execute " + joinPoint.getSignature().toString() + " method is " + timeElapsed);
        return returtObject;
    }
    @AfterThrowing(value = "execution(* com.dealership.car..*.*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception){
        log.error(joinPoint.getSignature().toString() + " An exception due to : " + exception.getMessage());

    }
}
