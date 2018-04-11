package com.stefanosiano.powerfulpermissions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicInteger;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiresPermissions {
    static final AtomicInteger atomicInteger = new AtomicInteger(0);

    int id = atomicInteger.getAndIncrement();
    int requestCode();
    String[] required();
    String[] optional() default "";
}

