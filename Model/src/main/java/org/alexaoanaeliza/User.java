package org.alexaoanaeliza;

import org.alexaoanaeliza.enums.Country;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final String personalNumber;
    private String phoneNumber;
    private final LocalDate birthday;
    private final String email;
    private final String password;
    private final Country country;
    private final String county;
    private final String city;
    private final String street;
    private final String number;
    private final String apartment;
    private Double investedAmount;
    private Double availableAmount;
    private Double usedAmount;

    public User() {
        super(0L);
        personalNumber = "";
        birthday = LocalDate.now();
        email = "";
        password = "";
        country = Country.ALBANIA;
        county = "";
        city = "";
        street = "";
        number = "";
        apartment = "";
        investedAmount = 0D;
        availableAmount = 0D;
        usedAmount = 0D;
    }

    public User(String firstName, String lastName, String personalNumber, String phoneNumber, LocalDate birthday, String email, String password, Country country, String county, String city, String street, String number, String apartment) {
        super(0L);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.country = country;
        this.county = county;
        this.city = city;
        this.street = street;
        this.number = number;
        this.apartment = apartment;
        investedAmount = 0D;
        availableAmount = 0D;
        usedAmount = 0D;
    }

    public User(Long id, String firstName, String lastName, String personalNumber, String phoneNumber, LocalDate birthday, String email, String password, Country country, String county, String city, String street, String number, String apartment) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.country = country;
        this.county = county;
        this.city = city;
        this.street = street;
        this.number = number;
        this.apartment = apartment;
        investedAmount = 0D;
        availableAmount = 0D;
        usedAmount = 0D;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getPassword() {
        return password;
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

    public Double getSold() {
        return availableAmount + usedAmount;
    }

    public Double getAvailableAmount() {
        return availableAmount;
    }

    public Double getInvestedAmount() {
        return investedAmount;
    }

    public Double getUsedAmount() {
        return usedAmount;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public void setInvestedAmount(Double investedAmount) {
        this.investedAmount = investedAmount;
    }

    public void setAvailableAmount(Double availableAmount) {
        this.availableAmount = availableAmount;
    }

    public void setUsedAmount(Double usedAmount) {
        this.usedAmount = usedAmount;
    }

    public void depositAmount(Double amount) {
        this.investedAmount += amount;
        this.availableAmount += amount;
    }

    public void withdrawAmount(Double amount) {
        this.investedAmount -= amount;
        this.availableAmount -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(personalNumber, user.personalNumber) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(birthday, user.birthday) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && country == user.country && Objects.equals(county, user.county) && Objects.equals(city, user.city) && Objects.equals(street, user.street) && Objects.equals(number, user.number) && Objects.equals(apartment, user.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, personalNumber, phoneNumber, birthday, email, password, country, county, city, street, number, apartment);
    }
}
