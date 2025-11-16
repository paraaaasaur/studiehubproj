package com.group5.springboot.annotation.dev;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to document structured detail for a {@link java.lang.Deprecated} target.
 * Always use along with {@link java.lang.Deprecated}.
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
public @interface DeprecatedDetail {
	String since() default "";
	String removeIn() default "";
	String replaceWith() default "";
	String[] reason() default "";
}