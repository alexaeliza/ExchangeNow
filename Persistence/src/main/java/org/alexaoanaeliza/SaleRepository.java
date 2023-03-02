package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.SaleRepositoryInterface;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.time.LocalDateTime;

public class SaleRepository implements SaleRepositoryInterface {
    private final String url;
    private final String username;
    private final String password;
    private static SaleRepository saleRepository;

    private SaleRepository() {
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

    public static SaleRepository getInstance() {
        if (saleRepository == null)
            saleRepository = new SaleRepository();
        return saleRepository;
    }

    private Sale extractSale(ResultSet resultSet) throws SQLException {
        return new Sale(resultSet.getLong("user"), resultSet.getLong("stock"),
                LocalDateTime.of(resultSet.getDate("date").toLocalDate(), resultSet.getTime("time").toLocalTime()),
                resultSet.getDouble("sum"));
    }

    @Override
    public Set<Sale> getAll() {
        return null;
    }

    @Override
    public Sale getById(Long aLong) {
        return null;
    }

    @Override
    public Sale add(Sale entity) {
        return null;
    }

    @Override
    public Sale delete(Long aLong) {
        return null;
    }

    @Override
    public Sale update(Sale entity) {
        return null;
    }

    @Override
    public Set<Sale> getSalesByUser(Long userId) {
        Set<Sale> sales = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Sales\" WHERE \"user\" = ?;");
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                sales.add(extractSale(resultSet));
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
        return sales;
    }
}
