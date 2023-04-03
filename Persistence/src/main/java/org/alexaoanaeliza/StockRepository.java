package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.StockRepositoryInterface;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class StockRepository implements StockRepositoryInterface {
    private final String url;
    private final String username;
    private final String password;
    private static StockRepository stockRepository;

    private StockRepository() {
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

    public static StockRepository getInstance() {
        if (stockRepository == null)
            stockRepository = new StockRepository();
        return stockRepository;
    }

    private Stock extractStock(ResultSet resultSet) throws SQLException {
        return new Stock(resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("companyName"));
    }

    @Override
    public Set<Stock> getAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Set<Stock> stocks = new HashSet<>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Stocks\";");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                stocks.add(extractStock(resultSet));
            return stocks;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Stock getById(Long id) {
        Stock stock = null;
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Stocks\" WHERE id = ?;");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                stock = extractStock(resultSet);
            return stock;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Stock add(Stock entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "\"Stocks\"(name, \"companyName\")" +
                    "VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getCompanyName());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Long id = resultSet.getLong(1);
                entity.setId(id);
            }
            return entity;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Stock delete(Long aLong) {
        return null;
    }

    @Override
    public Stock update(Stock entity) {
        return null;
    }

    @Override
    public Stock getStockBySale(Long saleId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Stocks\" INNER JOIN \"Sales\" WHERE \"Sales\".id = ? ON \"Stocks\".id = \"Sales\".userId;");
            preparedStatement.setLong(1, saleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractStock(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Stock getStockByPurchase(Long purchaseId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Stocks\" INNER JOIN \"Purchases\" WHERE \"Purchases\".id = ? ON \"Stocks\".id = \"Purchases\".userId;");
            preparedStatement.setLong(1, purchaseId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractStock(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Double getStockPriceByDate(Long stockId, LocalDate localDate) {
        return null;
    }

    @Override
    public Stock getStockByName(String name) {
        Stock stock = null;
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Stocks\" WHERE name = ?;");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                stock = extractStock(resultSet);
            return stock;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }
}
