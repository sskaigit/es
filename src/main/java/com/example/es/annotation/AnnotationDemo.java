package com.example.es.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AnnotationDemo {
    String value() default "嘿嘿嘿";
    String color() default "蓝色";
}
