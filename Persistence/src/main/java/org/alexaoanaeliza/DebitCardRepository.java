package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.DebitCardRepositoryInterface;
import org.alexaoanaeliza.enums.DebitCardType;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class DebitCardRepository implements DebitCardRepositoryInterface {
    private final String url;
    private final String username;
    private final String password;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private static DebitCardRepository debitCardRepository;

    private DebitCardRepository() {
        Properties properties = new Properties();
        try {
            properties.load(DebitCardRepository.class.getResourceAsStream("/config.properties"));
        } catch (IOException ioException) {
            throw new FileException(ioException.getMessage());
        }

        this.url = properties.getProperty("url");
        this.username = properties.getProperty("username");
        this.password = properties.getProperty("password");
        this.bankAccountRepository = BankAccountRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    public static DebitCardRepository getInstance() {
        if (debitCardRepository == null)
            debitCardRepository = new DebitCardRepository();
        return debitCardRepository;
    }

    private DebitCard extractDebitCard(ResultSet resultSet) throws SQLException {
        return new DebitCard(resultSet.getLong("id"),
                bankAccountRepository.getById(resultSet.getLong("bankAccountId")),
                DebitCardType.valueOf(resultSet.getString("debitCardType")),
                resultSet.getString("cardNumber"), resultSet.getString("cvv"),
                resultSet.getDate("expireDate").toLocalDate(),
                userRepository.getById(resultSet.getLong("userId")));
    }

    @Override
    public Set<DebitCard> getAll() {
        Set<DebitCard> debitCards = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM \"DebitCards\";");
            while (resultSet.next())
                debitCards.add(extractDebitCard(resultSet));
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
        return debitCards;
    }

    @Override
    public DebitCard getById(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"DebitCards\" WHERE \"DebitCards\".id = ?;");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractDebitCard(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public DebitCard add(DebitCard entity) {
        return null;
    }

    @Override
    public DebitCard delete(Long id) {
        return null;
    }

    @Override
    public DebitCard update(DebitCard debitCard) {
        return null;
    }

    @Override
    public void deleteAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM \"DebitCards\";");
            preparedStatement.execute();
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }
}
