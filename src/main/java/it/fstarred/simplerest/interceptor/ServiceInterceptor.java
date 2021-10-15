package it.fstarred.simplerest.interceptor;

import it.fstarred.simplerest.annotation.Analytics;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServiceUnavailableException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Used in conjuction with @{@link it.fstarred.simplerest.annotation.Analytics} annotation.
 * Log method parameters (if showArgs = true) and execution time.
 * If limit < method execution time, a logging warn will be displayed.
 */
@Analytics
@Priority(Priorities.USER)
@Interceptor
public class ServiceInterceptor {

    @Inject
    Logger logger;

    @AroundInvoke
    public Object logMethodEntry(InvocationContext ctx) throws Exception {
        // Analytics
        final Method method = ctx.getMethod();
        boolean isClassAnnotatedAnalytics = false;
        StopWatch stopWatch = null;
        int limit = 0;
        boolean showArgs = false;
        final StringBuilder payloadMessage = new StringBuilder();
        final StringBuilder messageLogArgs = new StringBuilder();
        final StringBuilder methodName = new StringBuilder(method.getName());

        final Object output;

        final Annotation[] classAnnotations = ctx.getMethod().getDeclaringClass().getAnnotations();
        final Annotation[] methodAnnotations = method.getAnnotations();
        final Object[] parameters = ctx.getParameters();
        final String className = ctx.getMethod().getDeclaringClass().getSimpleName();

        final Optional<Annotation> analyticsClassDeclaration = Arrays.stream(classAnnotations)
                .filter(Analytics.class::isInstance)
                .findAny();

        if (analyticsClassDeclaration.isPresent()) {
            final Analytics analytics = (Analytics)analyticsClassDeclaration.get();

            isClassAnnotatedAnalytics = true;

            stopWatch = new StopWatch();
            limit = analytics.limit();
            payloadMessage.append(analytics.payload());
            showArgs = analytics.showArgs();
        }

        final Optional<Annotation> analyticsMethodDeclaration = Arrays.stream(methodAnnotations)
                .filter(Analytics.class::isInstance)
                .findAny();

        if (analyticsMethodDeclaration.isPresent()) {
            final Analytics analytics = (Analytics)analyticsMethodDeclaration.get();
            if (!isClassAnnotatedAnalytics) {
                stopWatch = new StopWatch();
            }
            limit = analytics.limit();
            final String payload = analytics.payload();
            if (StringUtils.isNotBlank(payload)) {
                if (payloadMessage.length() > 0) {
                    payloadMessage.append(" - ");
                }
                payloadMessage.append(payload);
            }
            showArgs = analytics.showArgs();
        }

        // method parameters
        methodName.append('(');
        if (showArgs) {
            for (Object arg : parameters) {
                messageLogArgs.append(arg);
                messageLogArgs.append(", ");
            }
            if (messageLogArgs.length() > 0) {
                messageLogArgs.deleteCharAt(messageLogArgs.length() -2);
            }
        }
        methodName.append(messageLogArgs);
        methodName.append(')');

        try {
            if (stopWatch != null) {
                stopWatch.start();
            }
            output = ctx.proceed();
        } catch (ProcessingException e) {
            if (ExceptionUtils.indexOfThrowable(e, SocketTimeoutException.class) != ArrayUtils.INDEX_NOT_FOUND) {
                throw new ServiceUnavailableException(e.getMessage());
            } else {
                throw new InternalServerErrorException(e.getMessage());
            }
        } finally {

            // Analytics
            if (stopWatch != null && stopWatch.isStarted()) {
                stopWatch.stop();
                if (limit > 0 && stopWatch.getTime() > limit) {
                    logger.warn("[ANALYTICS]: {}.{} in {} ms [{}]", className, methodName, stopWatch.getTime(), payloadMessage);
                } else {
                    logger.info("[ANALYTICS]: {}.{} in {} ms [{}]", className, methodName, stopWatch.getTime(), payloadMessage);
                }
            }
        }

        return output;
    }

}
