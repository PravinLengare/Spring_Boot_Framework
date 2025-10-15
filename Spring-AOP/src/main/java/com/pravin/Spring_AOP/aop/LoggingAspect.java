package com.pravin.Spring_AOP.aop;

import ch.qos.logback.classic.joran.action.LoggerAction;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution (* com.pravin.Spring_AOP.service.JobService.getJob(..) )")
    public void logMethodCall(JoinPoint jp){

        LOGGER.info("method called ! "+jp.getSignature().getName());
    }

    @After("execution(* com.pravin.Spring_AOP.service.JobService.getJob(..) ) || execution (* com.pravin.Spring_AOP.service.JobService.updateJob(..) )")
    public void logMethodExecuted(JoinPoint jp){

        LOGGER.info("method executed ! "+jp.getSignature().getName());
    }

    @AfterThrowing("execution(* com.pravin.Spring_AOP.service.JobService.getJob(..) ) || execution (* com.pravin.Spring_AOP.service.JobService.updateJob(..) )")
    public void logMethodExecutedThrowing(JoinPoint jp){

        LOGGER.info("method has some issue ! "+jp.getSignature().getName());
    }

    @AfterReturning("execution(* com.pravin.Spring_AOP.service.JobService.getJob(..) ) || execution (* com.pravin.Spring_AOP.service.JobService.updateJob(..) )")
    public void logMethodAfterReturning(JoinPoint jp){

        LOGGER.info("method has executed successfully ! "+jp.getSignature().getName());
    }


}
