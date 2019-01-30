package com.example.es.aspect;

import com.example.es.annotation.AnnotationDemo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AnnotationAspect {

    @Pointcut("@annotation(com.example.es.annotation.AnnotationDemo)")
    public void userAnnotationPointcut(){

    }

    @Around(value = "userAnnotationPointcut()")
    public void processUserAnnotationJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("拦截到了" + joinPoint.getSignature().getName() +"方法...");
        AnnotationDemo annotation = getAnnotation(joinPoint);
        String color = annotation.color();
        String value = annotation.value();
        System.out.println("color :" +color +" || value : " +value);

        Object aThis = joinPoint.getThis();
        System.out.println("aThis: "+aThis);
        Object[] args = joinPoint.getArgs();
        System.out.println("args : "+args);
        for ( Object object: args) {
            System.out.println("object : "+object);
        }
        System.out.println("proceed : "+joinPoint.proceed());
    }

    private static AnnotationDemo getAnnotation(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(AnnotationDemo.class);
    }
}
