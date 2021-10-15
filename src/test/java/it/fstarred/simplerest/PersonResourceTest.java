package it.fstarred.simplerest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ValidatableResponse;
import it.fstarred.simplerest.model.Person;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Month;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class PersonResourceTest {

    @Test
    void hello_with_success() {
        given()
                .when()
                .queryParam("name", "Foo")
                .queryParam("country", "Italy")
                .queryParam("gender", "M")
                .get("/person/hello")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(is("hello Foo Doe of Italy, gender: M"));
    }

    @Test
    void hello_with_no_credentials() {
        given()
                .when()
                .queryParam("country", "Italy")
                .queryParam("gender", "M")
                .get("/person/hello")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
        ;
    }

    @Test
    void hello_with_no_gender() {
        given()
                .when()
                .queryParam("surname", "Surfoo")
                .queryParam("country", "Italy")
                .get("/person/hello")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
        ;
    }

    @Test
    void create() {

        final var person = new Person();
        person.setName("John");
        person.setSurname("Doe");
        person.setBirth(LocalDate.of(2020, Month.FEBRUARY, 23));
        person.setCity("Rome");
        person.setCountry("Italy");
        person.setGender("M");

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(person, ObjectMapperType.JSONB)
                .post("/person")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                ;
    }


    @Test
    void gimme_people() {
        given()
                .when()
                .queryParam("surname", "Doe")
                .queryParam("country", "California")
                .queryParam("gender", "M")
                .get("/person")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("surname", everyItem(equalTo("Doe")));
    }
}
