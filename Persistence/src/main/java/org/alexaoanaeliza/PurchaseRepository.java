package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.PurchaseRepositoryInterface;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class PurchaseRepository implements PurchaseRepositoryInterface {
    private final String url;
    private final String username;
    private final String password;
    private static PurchaseRepository purchaseRepository;

    private PurchaseRepository() {
        Properties properties = new Properties();
        try {
            properties.load(DebitCardRepository.class.getResourceAsStream("/config.properties"));
        } catch (IOException ioException) {
            throw new FileException(ioException.getMessage());
        }

        this.url = properties.getProperty("url");
        this.username = properties.getProperty("username");
        this.password = properties.getProperty("password");
    }

    public static PurchaseRepository getInstance() {
        if (purchaseRepository == null)
            purchaseRepository = new PurchaseRepository();
        return purchaseRepository;
    }

    private Purchase extractPurchase(ResultSet resultSet) throws SQLException {
        return new Purchase(resultSet.getLong("user"), resultSet.getLong("stock"),
                LocalDateTime.of(resultSet.getDate("date").toLocalDate(), resultSet.getTime("time").toLocalTime()),
                resultSet.getDouble("sum"));
    }

    @Override
    public Set<Purchase> getPurchasesByUser(Long userId) {
        Set<Purchase> purchases = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Purchases\" WHERE \"user\" = ?;");
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                purchases.add(extractPurchase(resultSet));
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
        return purchases;
    }

    @Override
    public Set<Purchase> getAll() {
        return null;
    }

    @Override
    public Purchase getById(Long aLong) {
        return null;
    }

    @Override
    public Purchase add(Purchase entity) {
        return null;
    }

    @Override
    public Purchase delete(Long aLong) {
        return null;
    }

    @Override
    public Purchase update(Purchase entity) {
        return null;
    }
}
