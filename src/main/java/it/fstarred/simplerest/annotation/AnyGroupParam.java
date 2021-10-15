package it.fstarred.simplerest.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * any param check that at least one query parameter is present for each group value
 */
@Inherited
@Retention(RUNTIME)
@Target({ PARAMETER })
public @interface AnyGroupParam {
    String value() default "default";
}
