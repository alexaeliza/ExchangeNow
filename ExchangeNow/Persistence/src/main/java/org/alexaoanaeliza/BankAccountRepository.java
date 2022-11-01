package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.RepositoryInterface;
import org.alexaoanaeliza.enums.Bank;
import org.alexaoanaeliza.enums.Currency;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class BankAccountRepository implements RepositoryInterface<Long, BankAccount> {
    private final String username;
    private final String password;
    private final String url;
    private static BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    private BankAccountRepository() {
        Properties properties = new Properties();
        try {
            properties.load(BankAccountRepository.class.getResourceAsStream("/config.properties"));
        } catch (IOException ioException) {
            throw new FileException(ioException.getMessage());
        }

        this.username = properties.getProperty("username");
        this.password = properties.getProperty("password");
        this.url = properties.getProperty("url");
        this.userRepository = UserRepository.getInstance();
    }

    public static BankAccountRepository getInstance() {
        if (bankAccountRepository == null)
            bankAccountRepository = new BankAccountRepository();
        return bankAccountRepository;
    }

    private BankAccount extractBankAccount(ResultSet resultSet) throws SQLException {
        return new BankAccount(resultSet.getLong("id"), resultSet.getString("iban"),
                Currency.valueOf(resultSet.getString("currency")),
                Bank.valueOf(resultSet.getString("bank")),
                userRepository.getById(resultSet.getLong("userId")));
    }

    @Override
    public Set<BankAccount> getAll() {
        Set<BankAccount> bankAccounts = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM \"BankAccounts\";");
            while (resultSet.next())
                bankAccounts.add(extractBankAccount(resultSet));
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
        return bankAccounts;
    }

    @Override
    public BankAccount getById(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"BankAccounts\" WHERE \"BankAccounts\".id = ?;");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractBankAccount(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public BankAccount add(BankAccount entity) {
        return null;
    }
}
