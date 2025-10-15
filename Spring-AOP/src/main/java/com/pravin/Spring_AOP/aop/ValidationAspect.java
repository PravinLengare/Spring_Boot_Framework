package com.pravin.Spring_AOP.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ValidationAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationAspect.class);
    @Around("execution(* com.pravin.Spring_AOP.service.JobService.getJob(..)) && args(postId)")
    public Object updateAndDelete(ProceedingJoinPoint pj,int postId) throws Throwable {
        if (postId < 0){
            LOGGER.info("postId is negative , updating it ");
            postId = -postId;
            LOGGER.info("new postID is : "+postId);
        }

        Object obj  = pj.proceed(new Object[]{postId});

        return obj;
    }
}
