package com.bank.history.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.bank.history.services.HistoryServiceImpl.*(..))")
    public void logBeforeHistoryServiceImplAdvice(JoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodClass = methodSignature.getDeclaringTypeName();
        String methodName = methodSignature.getName();
        String args = Arrays.toString(methodSignature.getParameterNames());
        log.info("logBeforeHistoryServiceImplAdvice: вызван метод {}.{} {}", methodClass, methodName, args);
    }

    @AfterReturning("execution(* com.bank.history.services.HistoryServiceImpl.*(..))")
    public void logAfterHistoryServiceImplAdvice(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodClass = methodSignature.getDeclaringTypeName();
        String methodName = methodSignature.getName();
        String args = Arrays.toString(methodSignature.getParameterNames());
        log.info("logAfterHistoryServiceImplAdvice: метод {}.{} {} успешно выполнен", methodClass, methodName, args);
    }

    @Before("execution(* com.bank.history.controllers.HistoryController.*(..))")
    public void logBeforeHistoryControllerAdvice(JoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodClass = methodSignature.getDeclaringTypeName();
        String methodName = methodSignature.getName();
        String args = Arrays.toString(methodSignature.getParameterNames());
        log.info("logBeforeHistoryControllerAdvice: вызван метод {}.{} {}", methodClass, methodName, args);
    }

    @AfterReturning("execution(* com.bank.history.controllers.HistoryController.*(..))")
    public void logAfterHistoryControllerAdvice(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodClass = methodSignature.getDeclaringTypeName();
        String methodName = methodSignature.getName();
        String args = Arrays.toString(methodSignature.getParameterNames());
        log.info("logAfterHistoryControllerAdvice: метод {}.{} {} успешно выполнен", methodClass, methodName, args);
    }

}
