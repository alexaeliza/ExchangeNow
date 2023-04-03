package org.alexaoanaeliza.server;

import org.alexaoanaeliza.*;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.enums.DebitCardType;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.ServiceException;
import org.alexaoanaeliza.service.ServiceInterface;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class Service implements ServiceInterface {
    private final UserRepository userRepository;
    private final DebitCardRepository debitCardRepository;
    private final StockRepository stockRepository;
    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private static Service service;

    private Service() {
        this.userRepository = UserRepository.getInstance();
        this.debitCardRepository = DebitCardRepository.getInstance();
        this.stockRepository = StockRepository.getInstance();
        this.saleRepository = SaleRepository.getInstance();
        this.purchaseRepository = PurchaseRepository.getInstance();
    }

    public static Service getInstance() {
        if (service == null)
            service = new Service();
        return service;
    }

    @Override
    public User loginUser(String email, String password) {
        User user = userRepository.getByEmail(email);
        if (user == null)
            throw new ServiceException("This email is not associated with any account");
        if (!user.getPassword().equals(password))
            throw new ServiceException("The password does not match this account");
        return user;
    }

    @Override
    public void addUser(String firstName, String lastName, String email, String password, String phoneNumber, String personalNumber, LocalDate birthday, Country country, String county, String city, String street, String number, String apartment) {
        User user = new User(firstName, lastName, personalNumber, phoneNumber, birthday, email, password, country, county, city,
                street, number, apartment);
        user = userRepository.add(user);
        if (user == null)
            throw new ServiceException("The account could not be created");
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return userRepository.getByEmail(email);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public void depositAmount(Double amount, DebitCard debitCard) {
        try {
            User owner = userRepository.getOwnerByDebitCard(debitCard.getOwnerId());
            owner.depositAmount(amount);
            userRepository.update(owner);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public void withdrawAmount(Double amount, DebitCard debitCard) {
        try {
            User owner = userRepository.getOwnerByDebitCard(debitCard.getOwnerId());
            owner.withdrawAmount(amount);
            userRepository.update(owner);
        } catch (DatabaseException databaseException) {
            throw new DatabaseException(databaseException.getMessage());
        }
    }

    @Override
    public DebitCard getDebitCardById(Long id) {
        try {
            return debitCardRepository.getById(id);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public DebitCard getDebitCardByData(String cardNumber, String cvv, LocalDate expireDate, DebitCardType debitCardType) {
        try {
            return debitCardRepository.getByData(debitCardType, cardNumber, cvv, expireDate);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public DebitCard addDebitCard(String cardNumber, String cvv, LocalDate expireDate, DebitCardType debitCardType, User owner) {
        try {
            DebitCard debitCard = new DebitCard(debitCardType, cardNumber, cvv, expireDate, owner.getId());
            return debitCardRepository.add(debitCard);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Set<Stock> getStocks() {
        try {
            return stockRepository.getAll();
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Double getTodaySoldByUser(User user) {
        try {
            return userRepository.getTodaySoldByUser(user.getId());
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Double getReturnValueByUser(User user) {
        try {
            return userRepository.getReturnValueByUser(user.getId());
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Double getReturnPercentageByUser(User user) {
        try {
            return userRepository.getReturnPercentageByUser(user.getId());
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Map<LocalDate, Double> getStockData(String stockId) {
        try {
            Map<LocalDate, Double> stockData = new PredictionService(stockId).getStockData();
            stockData.forEach((key, value) -> System.out.println(key + " -> " + value));
            return stockData;
        } catch (Exception exception) {
            throw new ServiceException(exception.getMessage());
        }
    }

    @Override
    public Sale sellStock(Long userId, Long stockId, LocalDateTime dateTime, Double sum) {
        try {
            Sale sale = new Sale(userId, stockId, dateTime, sum);
            return saleRepository.add(sale);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Purchase buyStock(Long userId, Long stockId, LocalDateTime dateTime, Double sum) {
        try {
            Purchase purchase = new Purchase(userId, stockId, dateTime, sum);
            return purchaseRepository.add(purchase);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }
}
