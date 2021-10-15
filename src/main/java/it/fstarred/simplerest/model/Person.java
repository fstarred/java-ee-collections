package it.fstarred.simplerest.model;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class Person {

    @NotEmpty
    @Size(min = 2)
    private String name;
    @NotEmpty
    @Size(min = 2)
    private String surname;
    @NotNull
    @Past
    private LocalDate birth;
    @NotEmpty
    @Size(min = 2)
    private String  country;
    @NotEmpty
    @Size(min = 2)
    private String city;
    @NotEmpty
    @Pattern(regexp = "\\b[MF]\\b")
    private String gender;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(final LocalDate birth) {
        this.birth = birth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }
}
