package org.alexaoanaeliza.protocol.request;

import org.alexaoanaeliza.enums.Country;

import java.time.LocalDate;

public class AddUserRequest implements Request {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String phoneNumber;
    private final String personalNumber;
    private final LocalDate birthday;
    private final Country country;
    private final String county;
    private final String city;
    private final String street;
    private final String number;
    private final String apartment;

    public AddUserRequest(String firstName, String lastName, String email, String password, String phoneNumber, String personalNumber, LocalDate birthday, Country country, String county, String city, String street, String number, String apartment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.personalNumber = personalNumber;
        this.birthday = birthday;
        this.country = country;
        this.county = county;
        this.city = city;
        this.street = street;
        this.number = number;
        this.apartment = apartment;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Country getCountry() {
        return country;
    }

    public String getCounty() {
        return county;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getApartment() {
        return apartment;
    }
}
