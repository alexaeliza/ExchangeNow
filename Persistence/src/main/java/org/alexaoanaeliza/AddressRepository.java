package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.RepositoryInterface;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class AddressRepository implements RepositoryInterface<Long, Address> {
    private final String username;
    private final String password;
    private final String url;
    private static AddressRepository addressRepository;

    private AddressRepository() {
        Properties properties = new Properties();
        try {
            properties.load(AddressRepository.class.getResourceAsStream("/config.properties"));
        } catch (IOException ioException) {
            throw new FileException(ioException.getMessage());
        }

        this.username = properties.getProperty("username");
        this.password = properties.getProperty("password");
        this.url = properties.getProperty("url");
    }

    public static AddressRepository getInstance() {
        if (addressRepository == null)
            addressRepository = new AddressRepository();
        return addressRepository;
    }

    private Address extractAddress(ResultSet resultSet) throws SQLException {
        return new Address(resultSet.getLong("id"), Country.valueOf(resultSet.getString("country")),
                resultSet.getString("county"), resultSet.getString("city"),
                resultSet.getString("street"), resultSet.getString("number"),
                resultSet.getString("apartment"));
    }

    @Override
    public void resetId() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE \"Addresses\" RESTART IDENTITY CASCADE;");
            preparedStatement.execute();
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Set<Address> getAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Addresses\";");
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Address> addresses = new HashSet<>();
            while (resultSet.next())
                addresses.add(extractAddress(resultSet));
            return addresses;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Address getById(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Addresses\" where \"Addresses\".id = ?;");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractAddress(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    private Long getLastAdded() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) FROM \"Addresses\";");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getLong(1);
            return 0L;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Address add(Address entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "\"Addresses\"(country, county, city, street, number, apartment) VALUES" +
                    "(?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, entity.getCountry().toString());
            preparedStatement.setString(2, entity.getCounty());
            preparedStatement.setString(3, entity.getCity());
            preparedStatement.setString(4, entity.getStreet());
            preparedStatement.setString(5, entity.getNumber());
            preparedStatement.setString(6, entity.getApartment());

            preparedStatement.execute();
            entity.setId(getLastAdded());
            return entity;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM \"Addresses\";");
            preparedStatement.execute();
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }
}