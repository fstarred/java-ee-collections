package it.fstarred.simplerest.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * xor param check that a query parameter is present once for each group value
 */
@Inherited
@Retention(RUNTIME)
@Target({ PARAMETER })
public @interface XorGroupParam {
    String value() default "default";
}
