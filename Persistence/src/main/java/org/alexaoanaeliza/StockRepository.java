package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.StockRepositoryInterface;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    public StockRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
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
            return stocks.stream().sorted(Comparator.comparing(Stock::getName)).collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Stock getById(Long id) {
        Stock stock = null;
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Stocks\" WHERE \"id\" = ?;");
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
                    "\"Stocks\"(\"name\", \"companyName\")" +
                    " VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT \"Stocks\".\"id\", \"Stocks\".\"name\", \"Stocks\".\"companyName\" FROM \"Stocks\" INNER JOIN \"Sales\" ON \"Stocks\".\"id\" = \"Sales\".\"stock\" WHERE \"Sales\".\"id\" = ?;");
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT \"Stocks\".\"id\", \"Stocks\".\"name\", \"Stocks\".\"companyName\" FROM \"Stocks\" INNER JOIN \"Purchases\" ON \"Stocks\".\"id\" = \"Purchases\".\"stock\" WHERE \"Purchases\".\"id\" = ?;");
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
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"StockPrices\" WHERE \"stock\" = ? AND \"date\" = ?;");
            preparedStatement.setLong(1, stockId);
            preparedStatement.setDate(2, Date.valueOf(localDate));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getDouble("price");
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Stock getStockByName(String name) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Stocks\" WHERE \"name\" = ?;");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractStock(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public void addPricesByStock(Map<LocalDate, Double> prices, Long stockId) {
        prices.forEach((date, price) -> addPriceByStock(date, price, stockId));
    }

    @Override
    public LocalDate getLastStockPriceByName(String stockName) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(\"StockPrices\".\"date\") FROM \"StockPrices\" " +
                    "INNER JOIN \"Stocks\" ON \"Stocks\".\"id\" = \"StockPrices\".\"stock\" WHERE \"Stocks\".\"name\" = ? GROUP BY \"StockPrices\".\"date\" ORDER BY \"StockPrices\".\"date\" DESC;");
            preparedStatement.setString(1, stockName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getDate(1).toLocalDate();
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Map<LocalDate, Double> getStockPrices(String stockName) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Map<LocalDate, Double> prices = new HashMap<>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"StockPrices\" " +
                    "INNER JOIN \"Stocks\" ON \"Stocks\".\"id\" = \"StockPrices\".\"stock\" WHERE \"Stocks\".\"name\" = ?;");
            preparedStatement.setString(1, stockName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                prices.put(resultSet.getDate("date").toLocalDate(), resultSet.getDouble("price"));
            return prices.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(LinkedHashMap::new, (map1, entry) -> map1.put(entry.getKey(), entry.getValue()), Map::putAll);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    private void addPriceByStock(LocalDate date, Double price, Long stockId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "\"StockPrices\"(\"date\", \"price\", \"stock\")" +
                    "VALUES (?, ?, ?)");
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setDouble(2, price);
            preparedStatement.setLong(3, stockId);

            preparedStatement.execute();
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }
}
