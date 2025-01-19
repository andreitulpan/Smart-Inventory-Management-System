package com.andreitech.sims.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // Pointcut for all service methods
    @Pointcut("execution(* com.andreitech.sims.service.*.*(..))")
    public void serviceMethods() {}

    // Log both before and after the method is called
    @Around("serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        // Log before method execution
        System.out.println("Method " + joinPoint.getSignature().getName() + " started execution!");

        // Proceed with method execution
        Object result = joinPoint.proceed();

        // Log after method execution
        System.out.println("Method: " + joinPoint.getSignature().getName() + " finished execution!");

        return result;
    }
}
