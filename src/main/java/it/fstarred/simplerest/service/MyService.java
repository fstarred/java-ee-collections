package it.fstarred.simplerest.service;

import it.fstarred.simplerest.annotation.Analytics;
import it.fstarred.simplerest.interceptor.ServiceInterceptor;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Analytics(payload = "my service")
@ApplicationScoped
public class MyService {

    @Inject
    Logger logger;

    @Analytics(showArgs = true, payload = "sleep method", limit = 4000)
    @Interceptors(ServiceInterceptor.class)
    public void sleep(int time) {
        try {
            Thread.sleep(time * 1000L);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
