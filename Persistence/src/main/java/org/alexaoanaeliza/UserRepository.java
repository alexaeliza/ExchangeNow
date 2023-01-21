package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.UserRepositoryInterface;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class UserRepository implements UserRepositoryInterface {
    private final String username;
    private final String password;
    private final String url;
    private static UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final VirtualAccountRepository virtualAccountRepository;

    private UserRepository() {
        Properties properties = new Properties();
        try {
            properties.load(UserRepository.class.getResourceAsStream("/config.properties"));
        } catch (IOException ioException) {
            throw new FileException(ioException.getMessage());
        }

        this.username = properties.getProperty("username");
        this.password = properties.getProperty("password");
        this.url = properties.getProperty("url");
        this.addressRepository = AddressRepository.getInstance();
        this.virtualAccountRepository = VirtualAccountRepository.getInstance();
    }

    public static UserRepository getInstance() {
        if (userRepository == null)
            userRepository = new UserRepository();
        return userRepository;
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getLong("id"), resultSet.getString("firstName"),
                resultSet.getString("lastName"), resultSet.getString("personalNumber"),
                addressRepository.getById(resultSet.getLong("address")),
                resultSet.getString("phoneNumber"), resultSet.getDate("birthday").toLocalDate(),
                resultSet.getString("email"), resultSet.getString("password"));
        user.setVirtualAccount(virtualAccountRepository.getByOwner(resultSet.getLong("id")));
        return user;
    }

    @Override
    public Set<User> getAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM \"Users\";");
            while (resultSet.next())
                users.add(extractUser(resultSet));
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
        return users;
    }

    @Override
    public User getById(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Users\" WHERE \"Users\".id = ?;");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractUser(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public User add(User entity) {
        Address address = addressRepository.add(entity.getAddress());
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "\"Users\"(\"firstName\", \"lastName\", email, password, \"personalNumber\", \"phoneNumber\", \"birthday\", \"address\")" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setString(5, entity.getPersonalNumber());
            preparedStatement.setString(6, entity.getPhoneNumber());
            preparedStatement.setDate(7, Date.valueOf(entity.getBirthday()));
            preparedStatement.setLong(8, address.getId());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Long id = resultSet.getLong(1);
                entity.setId(id);
            }

            VirtualAccount virtualAccount = virtualAccountRepository.add(entity.getVirtualAccount());
            entity.setVirtualAccount(virtualAccount);
            return entity;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public User delete(Long id) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User getByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Users\" WHERE \"Users\".email = ?;");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractUser(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }
}
