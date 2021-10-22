package it.fstarred.simplerest.rest;

import it.fstarred.simplerest.annotation.AnyGroupParam;
import it.fstarred.simplerest.annotation.QueryParamValidator;
import it.fstarred.simplerest.annotation.XorGroupParam;
import it.fstarred.simplerest.model.Person;
import it.fstarred.simplerest.provider.PersonDB;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/person")
public class PersonResource {

    @Inject
    Logger logger;

    @Inject
    PersonDB personDB;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Person create(@PathParam("id") int id) {
        return personDB.get(id);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person create(@Valid Person input) {
        personDB.add(input);
        return input;
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") int id) {
        personDB.delete(id);
    }

    /**
     * see https://tools.ietf.org/html/rfc6902 for further explanations
     */
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public void modify(@PathParam("id") int id,
                       JsonArray input) {
        personDB.update(id, input);
    }

    @QueryParamValidator
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(
            @AnyGroupParam @QueryParam("name") String name,
            @AnyGroupParam @QueryParam("surname") String surname,
            @XorGroupParam @QueryParam("city") String city,
            @XorGroupParam @QueryParam("country") String country,
            @XorGroupParam("xg0") @QueryParam("gender") @Pattern(regexp = "\\b([MF])\\b") String gender
    ) {
        logger.info("hello {} {} of {}, gender: {}", name, surname, city, gender );
        return MessageFormat.format("hello {0} {1} of {2}, gender: {3}",
                Optional.ofNullable(name).orElse("John"),
                Optional.ofNullable(surname).orElse("Doe"),
                Optional.ofNullable(city).orElse(country),
                gender);
    }

    @QueryParamValidator
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> search(
            @AnyGroupParam @QueryParam("name") String name,
            @AnyGroupParam @QueryParam("surname") String surname,
            @XorGroupParam @QueryParam("city") String city,
            @XorGroupParam @QueryParam("country") String country,
            @XorGroupParam("xg0") @QueryParam("gender") @Pattern(regexp = "\\b([MF])\\b") String gender,
            @QueryParam("sort") @DefaultValue("name:desc") String sort
    ) {
        final List<Person> people = PersonDB.getPeople();
        return people.stream()
                .filter(p -> name == null || name.equals(p.getName()))
                .filter(p -> surname == null || surname.equals(p.getSurname()))
                .filter(p -> city == null || city.equals(p.getCity()))
                .filter(p -> country == null || country.equals(p.getCountry()))
                .filter(p -> gender == null || gender.equals(p.getGender()))
                .collect(Collectors.toList())
                ;
    }
}
