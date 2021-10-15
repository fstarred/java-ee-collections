package it.fstarred.simplerest.provider;

import io.quarkus.runtime.Startup;
import it.fstarred.simplerest.model.Person;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.Month;

@Startup
public class PersonInitializer {

    @Inject
    Logger logger;

    @PostConstruct
    public void init() {
        logger.info("hello");
        createPerson("Mario", "Rossi", LocalDate.of(1970, Month.FEBRUARY, 3), "Rome", "Italy", "M");
        createPerson("Fabio", "Rossi", LocalDate.of(1973, Month.APRIL, 15), "Rome", "Italy", "M");
        createPerson("John","Doe", LocalDate.of(1980, Month.AUGUST, 22), "Los Angeles", "California", "M");
        createPerson("John","Gunn", LocalDate.of(1987, Month.AUGUST, 6), "Los Angeles", "California", "M");
        createPerson("Maria","Lopez", LocalDate.of(1984, Month.DECEMBER, 13), "Rio", "Brazil", "F");
    }

    private void createPerson(String name,
                              String surname,
                              LocalDate birth,
                              String city,
                              String country,
                              String gender) {
        final var output = new Person();
        output.setName(name);
        output.setSurname(surname);
        output.setBirth(birth);
        output.setCountry(country);
        output.setCity(city);
        output.setGender(gender);
        PersonDB.add(output);
    }

}
