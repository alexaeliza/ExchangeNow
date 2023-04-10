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

    public SaleRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static SaleRepository getInstance() {
        if (saleRepository == null)
            saleRepository = new SaleRepository();
        return saleRepository;
    }

    private Sale extractSale(ResultSet resultSet) throws SQLException {
        Sale sale = new Sale(resultSet.getLong("user"), resultSet.getLong("stock"),
                LocalDateTime.of(resultSet.getDate("date").toLocalDate(), resultSet.getTime("time").toLocalTime()),
                resultSet.getDouble("sum"));
        sale.setId(resultSet.getLong("id"));
        return sale;
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
    public Sale add(Sale sale) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "\"Sales\"(\"user\", \"stock\", \"date\", \"time\", \"sum\")" +
                    " VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, sale.getUserId());
            preparedStatement.setLong(2, sale.getStockId());
            preparedStatement.setDate(3, Date.valueOf(sale.getDateTime().toLocalDate()));
            preparedStatement.setTime(4, Time.valueOf(sale.getDateTime().toLocalTime()));
            preparedStatement.setDouble(5, sale.getSum());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Long id = resultSet.getLong(1);
                sale.setId(id);
            }
            return sale;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Sales\" WHERE \"Sales\".\"user\" = ?;");
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
