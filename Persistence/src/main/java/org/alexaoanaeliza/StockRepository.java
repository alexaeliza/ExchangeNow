package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.StockRepositoryInterface;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.*;
import java.sql.*;
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

    @Override
    public Set<Stock> getAll() {
        return null;
    }

    @Override
    public Stock getById(Long aLong) {
        return null;
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
}
