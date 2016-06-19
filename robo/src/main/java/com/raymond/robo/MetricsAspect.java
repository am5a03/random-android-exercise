package com.raymond.robo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

/**
 * Created by Raymond on 2016-04-24.
 */
@Aspect
public class MetricsAspect {

    @Pointcut("execution(@com.raymond.robo.Metrics * *(..))")
    public void methodAnnoatedWithMetrics(){}

    @Pointcut("execution(@com.raymond.robo.Metrics *.new(..))")
    public void constructorWithMetrics(){}

    @Around("methodAnnoatedWithMetrics() || constructorWithMetrics()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Metrics annotation = methodSignature.getMethod().getAnnotation(Metrics.class);
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        Object result = joinPoint.proceed();

        MetricsLog.logEvent(annotation.eventAction(), annotation.eventLabel(), annotation.eventName());

        return result;
    }
}
