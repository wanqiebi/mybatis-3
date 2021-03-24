package org.apache.ibatis.annotations;

import org.apache.ibatis.session.IConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @since 3.2.2.m1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MapValue {
    String value() default "";
    Class<? extends IConverter> converter() default IConverter.class;
}
