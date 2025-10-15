package com.pravin.Spring_AOP.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceMonitorAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceMonitorAspect.class);

    @Around("execution(* com.pravin.Spring_AOP.service.JobService.*(..))")
    public Object monitorTime(ProceedingJoinPoint pj) throws Throwable {

        long start = System.currentTimeMillis();
        Object obj = pj.proceed();
        long end = System.currentTimeMillis();

        LOGGER.info("Time Taken by : "+pj.getSignature().getName() + " is : " +(end - start)+" ms ");

        return obj;
    }
}

