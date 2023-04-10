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

    public PurchaseRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static PurchaseRepository getInstance() {
        if (purchaseRepository == null)
            purchaseRepository = new PurchaseRepository();
        return purchaseRepository;
    }

    private Purchase extractPurchase(ResultSet resultSet) throws SQLException {
        Purchase purchase = new Purchase(resultSet.getLong("user"), resultSet.getLong("stock"),
                LocalDateTime.of(resultSet.getDate("date").toLocalDate(), resultSet.getTime("time").toLocalTime()),
                resultSet.getDouble("sum"));
        purchase.setId(resultSet.getLong("id"));
        return purchase;
    }

    @Override
    public Set<Purchase> getPurchasesByUser(Long userId) {
        Set<Purchase> purchases = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Purchases\" WHERE \"Purchases\".\"user\" = ?;");
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
    public Purchase add(Purchase purchase) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "\"Purchases\"(\"user\", \"stock\", \"date\", \"time\", \"sum\")" +
                    " VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, purchase.getUserId());
            preparedStatement.setLong(2, purchase.getStockId());
            preparedStatement.setDate(3, Date.valueOf(purchase.getDateTime().toLocalDate()));
            preparedStatement.setTime(4, Time.valueOf(purchase.getDateTime().toLocalTime()));
            preparedStatement.setDouble(5, purchase.getSum());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Long id = resultSet.getLong(1);
                purchase.setId(id);
            }
            return purchase;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
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
