package com.example.springbatchdemo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TracePerformanceAspect {

    private final Logger logger = LoggerFactory.getLogger(TracePerformanceAspect.class);

   /* @Around ("execution(* com.example.springbatchdemo..*.*(..)))")
    public Object logTracePerformanceAspect(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        //Log method execution time
        logger.info("Execution time of {}.{} :: {} ms", className, methodName, (end - start));

        return result;
    }*/
}