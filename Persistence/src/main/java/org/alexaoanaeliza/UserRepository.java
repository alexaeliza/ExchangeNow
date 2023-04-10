package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.UserRepositoryInterface;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class UserRepository implements UserRepositoryInterface {
    private final String username;
    private final String password;
    private final String url;
    private static UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final SaleRepository saleRepository;
    private final StockRepository stockRepository;

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
        purchaseRepository = PurchaseRepository.getInstance();
        saleRepository = SaleRepository.getInstance();
        stockRepository = StockRepository.getInstance();
    }

    public UserRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        purchaseRepository = new PurchaseRepository(url, username, password);
        saleRepository = new SaleRepository(url, username, password);
        stockRepository = new StockRepository(url, username, password);
    }

    public static UserRepository getInstance() {
        if (userRepository == null)
            userRepository = new UserRepository();
        return userRepository;
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getLong("id"), resultSet.getString("firstName"),
                resultSet.getString("lastName"), resultSet.getString("personalNumber"),
                resultSet.getString("phoneNumber"), resultSet.getDate("birthday").toLocalDate(),
                resultSet.getString("email"), resultSet.getString("password"),
                Country.valueOf(resultSet.getString("country")), resultSet.getString("county"),
                resultSet.getString("city"), resultSet.getString("street"),
                resultSet.getString("number"), resultSet.getString("apartment"));
        user.setAvailableAmount(resultSet.getDouble("availableAmount"));
        user.setInvestedAmount(resultSet.getDouble("investedAmount"));
        user.setUsedAmount(resultSet.getDouble("usedAmount"));
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Users\" WHERE \"Users\".\"id\" = ?;");
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
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "\"Users\"(\"firstName\", \"lastName\", \"email\", \"password\", \"personalNumber\", \"phoneNumber\", " +
                    "\"birthday\", \"country\", \"county\", \"city\", \"street\", \"number\", \"apartment\", " +
                    "\"investedAmount\", \"availableAmount\", \"usedAmount\")" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setString(5, entity.getPersonalNumber());
            preparedStatement.setString(6, entity.getPhoneNumber());
            preparedStatement.setDate(7, Date.valueOf(entity.getBirthday()));
            preparedStatement.setString(8, entity.getCountry().toString());
            preparedStatement.setString(9, entity.getCounty());
            preparedStatement.setString(10, entity.getCity());
            preparedStatement.setString(11, entity.getStreet());
            preparedStatement.setString(12, entity.getNumber());
            preparedStatement.setString(13, entity.getApartment());
            preparedStatement.setDouble(14, entity.getInvestedAmount());
            preparedStatement.setDouble(15, entity.getAvailableAmount());
            preparedStatement.setDouble(16, entity.getUsedAmount());

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
    public User delete(Long id) {
        return null;
    }

    @Override
    public User update(User user) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE \"Users\" SET \"investedAmount\" = ?, \"availableAmount\" = ? WHERE \"id\" = ?;");
            preparedStatement.setDouble(1, user.getInvestedAmount());
            preparedStatement.setDouble(2, user.getAvailableAmount());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.execute();
            return user;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Users\" WHERE \"Users\".\"email\" = ?;");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractUser(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public User getOwnerByDebitCard(Long debitCardId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"Users\" INNER JOIN \"DebitCards\" ON \"Users\".id = \"DebitCards\".owner WHERE \"DebitCards\".id = ?;");
            preparedStatement.setLong(1, debitCardId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractUser(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Double getTodaySoldByUser(Long userId) {
        AtomicReference<Double> sold = new AtomicReference<>(0D);
        Set<Purchase> purchases = purchaseRepository.getPurchasesByUser(userId);
        Set<Sale> sales = saleRepository.getSalesByUser(userId);
        purchases.forEach(purchase -> {
            Stock stock = stockRepository.getStockByPurchase(purchase.getId());
            sold.updateAndGet(v -> v + stock.getCurrentPrice() * purchase.getSum() /
                    stockRepository.getStockPriceByDate(stock.getId(), purchase.getDateTime().toLocalDate()));
        });
        sales.forEach(sale -> {
            Stock stock = stockRepository.getStockByPurchase(sale.getId());
            sold.updateAndGet(v -> v - stock.getCurrentPrice() * sale.getSum() /
                    stockRepository.getStockPriceByDate(stock.getId(), sale.getDateTime().toLocalDate()));
        });
        return sold.get() + getById(userId).getAvailableAmount();
    }

    @Override
    public Double getReturnValueByUser(Long userId) {
        return getById(userId).getInvestedAmount() - getTodaySoldByUser(userId);
    }

    @Override
    public Double getReturnPercentageByUser(Long userId) {
        Double investedAmount = getById(userId).getInvestedAmount();
        if (investedAmount.equals(0D))
            return 0D;
        return Math.abs(getReturnValueByUser(userId)) / investedAmount;
    }
}
