package it.fstarred.simplerest.provider;

import it.fstarred.simplerest.model.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.json.*;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class PersonDB {

    private PersonDB() {
    }

    static AtomicInteger id = new AtomicInteger();

    private static final List<Person> people = new ArrayList<>();

    public static List<Person> getPeople() {
        return Collections.unmodifiableList(people);
    }

    public Person get(int id) {
        return people
                .stream()
                .filter(i -> i.getId() == id)
                .findAny()
                .orElseThrow(NotFoundException::new)
                ;
    }

    public void update(int id, final JsonArray input) {

        final Person person = people.stream()
                .filter(i -> i.getId() == id)
                .findAny()
                .orElseThrow(NotFoundException::new);

        final JsonObject source; // json object person representation

        try (JsonReader jsonReader = Json.createReader(new StringReader(Json.createValue(JsonbBuilder.create().toJson(person)).getString()))) {
            source = jsonReader.readObject();
        }

        final JsonObject output; // apply update to source object
        try {
            output = Json.createPatch(input).apply(source);
        } catch (JsonException e) {
            throw new BadRequestException(e.getMessage());
        }

        try (final Jsonb jsonb = JsonbBuilder.create()) {
            final Person target = jsonb.fromJson(output.toString(), Person.class);
            person.setName(target.getName());
            person.setSurname(target.getSurname());
            person.setCountry(target.getCountry());
            person.setCity(target.getCity());
            person.setGender(target.getGender());
            person.setBirth(target.getBirth());
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
    }

    public void add(Person person) {
        person.setId(id.addAndGet(1));
        people.add(person);
    }

    public void delete(int id) {
        final boolean b = people.removeIf(i -> i.getId() == id);
        if (!b) {
            throw new NotFoundException("id not found: " + id);
        }
    }

    public void create(String name,
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
        add(output);
    }
}
