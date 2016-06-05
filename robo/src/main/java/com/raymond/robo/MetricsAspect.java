package com.raymond.robo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by Raymond on 2016-04-24.
 */
@Aspect
public class MetricsAspect {
    private static final String POINTCUT_METHOD
            = "execution(@com.raymond.robo.Metrics * *(..))";
    private static final String POINTCUT_CONSTRUCTOR
            = "execution(@com.raymond.robo.Metrics *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnoatedWithMetrics(){}

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorWithMetrics(){}

    @Around("methodAnnoatedWithMetrics() || constructorWithMetrics()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        Object result = joinPoint.proceed();

        MetricsLog.logEvent(className, methodName, "");

        return result;
    }
}
