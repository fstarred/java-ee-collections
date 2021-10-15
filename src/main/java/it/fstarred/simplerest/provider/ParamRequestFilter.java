package it.fstarred.simplerest.provider;

import it.fstarred.simplerest.annotation.AnyGroupParam;
import it.fstarred.simplerest.annotation.QueryParamValidator;
import it.fstarred.simplerest.annotation.XorGroupParam;
import org.slf4j.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Priorities;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Validator for query parameters
 */
@Provider
@QueryParamValidator
@Priority(Priorities.ENTITY_CODER)
public class ParamRequestFilter implements ContainerRequestFilter {

    @Inject
    Logger logger;

    @Context
    ResourceInfo resinfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {

        final Method resourceMethod = resinfo.getResourceMethod();
        final Annotation[][] parameterAnnotations = resourceMethod.getParameterAnnotations();
        final MultivaluedMap<String, String> queryParameters = containerRequestContext.getUriInfo().getQueryParameters();
        final Map<String, Integer> xorQueryParameterMap = new HashMap<>(); // xor-params group counter
        final Map<String, Integer> anyQueryParameterMap = new HashMap<>(); // any-params group counter

        for (Annotation[] annotations : parameterAnnotations) {
            final Optional<QueryParam> optionalQueryParam = Arrays
                    .stream(annotations)
                    .filter(QueryParam.class::isInstance)
                    .map(QueryParam.class::cast)
                    .findAny();

            optionalQueryParam.ifPresent(queryParam -> {
                final String id = queryParam.value();

                // xor
                Arrays
                        .stream(annotations)
                        .filter(XorGroupParam.class::isInstance)
                        .map(XorGroupParam.class::cast)
                        .findAny()
                        .ifPresent(xor -> {
                            final String xorId = xor.value();
                            if (!xorQueryParameterMap.containsKey(xorId)) {
                                xorQueryParameterMap.put(xorId, 0);
                            }
                            Optional.ofNullable(queryParameters.get(id))
                                    .flatMap(list -> list.stream()
                                            .filter(o -> !o.isEmpty())
                                            .findAny()).ifPresent(val -> {
                                        Integer count = xorQueryParameterMap.get(xorId);
                                        xorQueryParameterMap.put(xorId, ++count);
                                    });
                        });

                // any
                Arrays
                        .stream(annotations)
                        .filter(AnyGroupParam.class::isInstance)
                        .map(AnyGroupParam.class::cast)
                        .findAny()
                        .ifPresent(any -> {
                            final String anyId = any.value();
                            if (!anyQueryParameterMap.containsKey(anyId)) {
                                anyQueryParameterMap.put(anyId, 0);
                            }
                            Optional.ofNullable(queryParameters.get(id))
                                    .flatMap(list -> list.stream()
                                            .filter(o -> !o.isEmpty())
                                            .findAny()).ifPresent(val -> {
                                        Integer count = anyQueryParameterMap.getOrDefault(anyId, 0);
                                        anyQueryParameterMap.put(anyId, ++count);
                                    });
                        });
            });

        }

        xorQueryParameterMap
                .entrySet()
                .stream()
                .filter(i -> i.getValue() != 1)
                .findAny()
                .ifPresent(r ->
                {
                    logger.error("Zero or more than one xor group parameter was specified: {}", r.getKey());
                    final Response errorResponse = Response.status(Response.Status.BAD_REQUEST).build();
                    containerRequestContext.abortWith(errorResponse);
                    throw new BadRequestException("Zero or more than one xor group parameter was specified: " + r.getKey());
                });

        anyQueryParameterMap
                .entrySet()
                .stream()
                .filter(i -> i.getValue() == 0)
                .findAny()
                .ifPresent(r ->
                {
                    logger.error("At least one any group parameter must be specified: {}", r.getKey());
                    final Response errorResponse = Response.status(Response.Status.BAD_REQUEST).build();
                    containerRequestContext.abortWith(errorResponse);
                    throw new BadRequestException("At least one any group parameter must be specified: " + r.getKey());
                });
    }
}