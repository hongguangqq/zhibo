package com.jyt.baseapp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by v7 on 2016/12/23.
 */
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ActivityAnnotation {
    public boolean showBack() default false;
    public String title() default "";
    public boolean showFunction() default false;
    public String functionText() default "";
    boolean showActionBar() default true;
}
