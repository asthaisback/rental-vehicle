package com.vehiclerental.vehicle_rental_system.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect // tells spring that Aspect class h and AOP related logic h
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class); //creates logger instance

    // Executes before the target method run
    @Before("execution(* com.vehiclerental.vehicle_rental_system.service.*.*(..))")
    public void logServiceMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();  // joint point is a specific point
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("Executing method: {}.{}", className, methodName);
    }
    @Before("execution(* com.vehiclerental.vehicle_rental_system.controller.*.*(..))")
    public void logControllerMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("API Accessed: {}.{}", className, methodName);
    }
}