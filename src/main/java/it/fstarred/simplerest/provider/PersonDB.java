package it.fstarred.simplerest.provider;

import it.fstarred.simplerest.model.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonDB {

    private PersonDB() {
    }

    private static final List<Person> people = new ArrayList<>();

    public static List<Person> getPeople() {
        return Collections.unmodifiableList(people);
    }

    public static void add(Person person) {
        people.add(person);
    }
}
