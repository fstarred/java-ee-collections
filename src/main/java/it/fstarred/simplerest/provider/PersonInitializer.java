package it.fstarred.simplerest.provider;

import io.quarkus.runtime.Startup;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;

@Startup
public class PersonInitializer {

    @Inject
    Logger logger;

    @Inject
    PersonDB personDB;

    @PostConstruct
    public void init() {
        logger.info("hello");
        personDB.create("Mario", "Rossi", LocalDate.of(1970, Month.FEBRUARY, 3), "Rome", "Italy", "M");
        personDB.create("Fabio", "Rossi", LocalDate.of(1973, Month.APRIL, 15), "Rome", "Italy", "M");
        personDB.create("John","Doe", LocalDate.of(1980, Month.AUGUST, 22), "Los Angeles", "California", "M");
        personDB.create("John","Gunn", LocalDate.of(1987, Month.AUGUST, 6), "Los Angeles", "California", "M");
        personDB.create("Maria","Lopez", LocalDate.of(1984, Month.DECEMBER, 13), "Rio", "Brazil", "F");
    }



}
