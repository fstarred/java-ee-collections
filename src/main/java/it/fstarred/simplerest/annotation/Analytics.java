package it.fstarred.simplerest.annotation;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Retention(RUNTIME)
@Target({ METHOD, TYPE })
public @interface Analytics {

    @Nonbinding int limit() default 5000;

    @Nonbinding String payload() default "";

    @Nonbinding boolean showArgs() default false;
}