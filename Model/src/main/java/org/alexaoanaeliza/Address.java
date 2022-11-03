package org.alexaoanaeliza;

import com.sun.jdi.request.DuplicateRequestException;
import org.alexaoanaeliza.enums.Country;

import java.io.Serializable;
import java.util.*;

public class Address extends Entity<Long> implements Serializable {
    private final Country country;
    private final String county;
    private final String city;
    private final String street;
    private final String number;
    private final String apartment;
    private final Set<User> tenants;

    public Address() {
        super(0L);
        this.country = Country.ALBANIA;
        this.county = "";
        this.city = "";
        this.street = "";
        this.number = "";
        this.apartment = "";
        this.tenants = new HashSet<>();
    }

    public Address(Country country, String county, String city, String street, String number, String apartment) {
        super(0L);
        this.country = country;
        this.county = county;
        this.city = city;
        this.street = street;
        this.number = number;
        this.apartment = apartment;
        this.tenants = new HashSet<>();
    }

    public Address(Long id, Country country, String county, String city, String street, String number, String apartment) {
        super(id);
        this.country = country;
        this.county = county;
        this.city = city;
        this.street = street;
        this.number = number;
        this.apartment = apartment;
        this.tenants = new HashSet<>();
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

    protected void addTenant(User user) {
        if (tenants.contains(user))
            throw new DuplicateRequestException("This user is already registered at this location");
        tenants.add(user);
    }

    protected void removeTenant(User user) {
        if (!tenants.contains(user))
            throw new NoSuchElementException("This user is not registered at this location");
        tenants.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (super.equals(o)) return true;
        Address address = (Address) o;
        return country == address.country && county.equals(address.county) && city.equals(address.city) && street.equals(address.street) && number.equals(address.number) && Objects.equals(apartment, address.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, county, city, street, number, apartment);
    }
}
