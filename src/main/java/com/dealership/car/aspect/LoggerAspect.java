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

/**
 * LoggerAspect is an aspect-oriented component that provides logging
 * functionality for methods in the com.dealership.car package.
 * It logs the execution details and exceptions of the methods.
 */
@Component
@Slf4j
@Aspect
public class LoggerAspect {
    /**
     * Logs the execution details of the method being invoked, including the start time,
     * end time, and the total time taken for the method to execute.
     *
     * @param joinPoint the proceeding join point representing the method being intercepted
     * @return the result of the method execution
     * @throws Throwable if the method being intercepted throws any exception
     */
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
    /**
     * Logs the details of an exception thrown during the execution of methods
     * in the com.dealership.car package.
     *
     * @param joinPoint Information about the method that threw the exception.
     * @param exception The thrown exception to be logged.
     */
    @AfterThrowing(value = "execution(* com.dealership.car..*.*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception){
        log.error(joinPoint.getSignature().toString() + " An exception due to : " + exception.getMessage());

    }
}
