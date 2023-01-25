package org.alexaoanaeliza;

import org.alexaoanaeliza.asbtractRepository.VirtualAccountRepositoryInterface;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Set;

public class VirtualAccountRepository implements VirtualAccountRepositoryInterface {
    private final String password;
    private final String username;
    private final String url;
    private static VirtualAccountRepository virtualAccountRepository;

    private VirtualAccountRepository() {
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

    public static VirtualAccountRepository getInstance() {
        if (virtualAccountRepository == null)
            virtualAccountRepository = new VirtualAccountRepository();
        return virtualAccountRepository;
    }

    @Override
    public Set<VirtualAccount> getAll() {
        return null;
    }

    @Override
    public VirtualAccount getById(Long aLong) {
        return null;
    }

    @Override
    public VirtualAccount getByOwner(Long ownerId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM \"VirtualAccounts\" WHERE owner = ? ;");
            preparedStatement.setLong(1, ownerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractVirtualAccount(resultSet);
            return null;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    private VirtualAccount extractVirtualAccount(ResultSet resultSet) throws SQLException {
        VirtualAccount virtualAccount = new VirtualAccount(resultSet.getLong("id"), null);
        virtualAccount.setAvailableSold(resultSet.getDouble("availableAmount"));
        virtualAccount.setInvestedAmount(resultSet.getDouble("investedAmount"));
        virtualAccount.setUsedSold(resultSet.getDouble("usedAmount"));
        return virtualAccount;
    }

    @Override
    public VirtualAccount add(VirtualAccount entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \"VirtualAccounts\"(\"investedAmount\", \"availableAmount\", \"usedAmount\", owner) VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, 0);
            preparedStatement.setDouble(2, 0);
            preparedStatement.setDouble(3, 0);
            preparedStatement.setLong(4, entity.getOwner().getId());
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
    public VirtualAccount delete(Long aLong) {
        return null;
    }

    @Override
    public VirtualAccount update(VirtualAccount entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE \"VirtualAccounts\" SET \"investedAmount\" = ?, \"availableAmount\" = ? WHERE id = ? ;");
            preparedStatement.setDouble(1, entity.getInvestedAmount());
            preparedStatement.setDouble(2, entity.getAvailableSold());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.execute();
            return entity;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }
}
