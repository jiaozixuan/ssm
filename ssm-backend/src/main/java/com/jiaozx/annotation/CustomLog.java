package com.jiaozx.annotation;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomLog {

    String title() default "" ;

    String type() default "";
}
