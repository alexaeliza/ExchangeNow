package org.alexaoanaeliza.server;

import org.alexaoanaeliza.*;
import org.alexaoanaeliza.asbtractRepository.*;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.enums.DebitCardType;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.ServiceException;
import org.alexaoanaeliza.service.ServiceInterface;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Service implements ServiceInterface {
    private final UserRepositoryInterface userRepository;
    private final DebitCardRepositoryInterface debitCardRepository;
    private final StockRepositoryInterface stockRepository;
    private final SaleRepositoryInterface saleRepository;
    private final PurchaseRepositoryInterface purchaseRepository;
    private static Service service;

    public Service(UserRepositoryInterface userRepository, DebitCardRepositoryInterface debitCardRepository,
                   StockRepositoryInterface stockRepository, SaleRepositoryInterface saleRepository,
                   PurchaseRepositoryInterface purchaseRepository) {
        this.userRepository = userRepository;
        this.debitCardRepository = debitCardRepository;
        this.stockRepository = stockRepository;
        this.saleRepository = saleRepository;
        this.purchaseRepository = purchaseRepository;
    }

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
        try {
            User user = userRepository.getByEmail(email);
            if (user == null)
                throw new ServiceException("This email is not associated with any account");
            if (!user.getPassword().equals(password))
                throw new ServiceException("The password does not match this account");
            return user;
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public void addUser(String firstName, String lastName, String email, String password, String phoneNumber, String personalNumber, LocalDate birthday, Country country, String county, String city, String street, String number, String apartment) {
        try {
            User user = new User(firstName, lastName, personalNumber, phoneNumber, birthday, email, password, country, county, city,
                    street, number, apartment);

            userRepository.add(user);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
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
            User owner = userRepository.getById(debitCard.getOwnerId());
            owner.depositAmount(amount);
            userRepository.update(owner);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public void withdrawAmount(Double amount, DebitCard debitCard) {
        try {
            User owner = userRepository.getById(debitCard.getOwnerId());
            owner.withdrawAmount(amount);
            userRepository.update(owner);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
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
            LocalDate lastDate = stockRepository.getLastStockPriceByName(stockId);
            if (lastDate == null) {
                Map<LocalDate, Double> prices = new PredictionService(stockId).getStockData();
                stockRepository.addPricesByStock(prices, stockRepository.getStockByName(stockId).getId());
                return prices;
            }
            if (!lastDate.isEqual(LocalDate.now())) {
                Map<LocalDate, Double> prices = new PredictionService(stockId).getStockData(lastDate.plusDays(1));
                stockRepository.addPricesByStock(prices, stockRepository.getStockByName(stockId).getId());
            }
            return stockRepository.getStockPrices(stockId);
        } catch (DatabaseException databaseException) {
            return stockRepository.getStockPrices(stockId);
        } catch (Exception exception) {
            throw new ServiceException(exception.getMessage());
        }
    }

    @Override
    public Sale sellStock(Long userId, Long stockId, LocalDateTime dateTime, Double sum) {
        try {
            Sale sale = new Sale(userId, stockId, dateTime, sum, sum / stockRepository.getStockPriceByDate(stockId, dateTime.toLocalDate()));
            sale = saleRepository.add(sale);
            User user = userRepository.getById(userId);
            user.setUsedAmount(user.getUsedAmount() - sum);
            user.setAvailableAmount(user.getAvailableAmount() + sum);
            userRepository.update(user);
            return sale;
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Purchase buyStock(Long userId, Long stockId, LocalDateTime dateTime, Double sum) {
        try {
            Purchase purchase = new Purchase(userId, stockId, dateTime, sum, sum / stockRepository.getStockPriceByDate(stockId, dateTime.toLocalDate()));
            purchase = purchaseRepository.add(purchase);
            User user = userRepository.getById(userId);
            user.setAvailableAmount(user.getAvailableAmount() - sum);
            user.setUsedAmount(user.getUsedAmount() + sum);
            userRepository.update(user);
            return purchase;
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Stock getStockByName(String name) {
        try {
            return stockRepository.getStockByName(name);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public User getUserById(Long id) {
        try {
            return userRepository.getById(id);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Map<Stock, Double> getPortfolioByUser(Long userId) {
        try {
            Set<Sale> sales = saleRepository.getSalesByUser(userId);
            Map<Stock, Double> portfolio = new HashMap<>();
            Set<Purchase> purchases = purchaseRepository.getPurchasesByUser(userId);
            purchases.forEach(purchase -> {
                Stock stock = stockRepository.getById(purchase.getStockId());
                if (portfolio.containsKey(stock))
                    portfolio.put(stock, portfolio.get(stock) + purchase.getSum());
                else
                    portfolio.put(stock, purchase.getSum());
            });
            sales.forEach(sale -> {
                Stock stock = stockRepository.getById(sale.getStockId());
                portfolio.put(stock, portfolio.get(stock) - sale.getSum());
            });

            portfolio.forEach((stock, sum) -> {
                if (sum == 0)
                    portfolio.remove(stock);
            });
            return portfolio;
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Stock getStockById(Long stockId) {
        try {
            return stockRepository.getById(stockId);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }

    @Override
    public Map<LocalDate, Double> getStockPredictions(String stockId, LocalDate startDate, LocalDate endDate) {
        try {
            return new PredictionService(stockId).getStockPredictions(startDate, endDate);
        } catch (DatabaseException databaseException) {
            return stockRepository.getStockPrices(stockId);
        } catch (Exception exception) {
            throw new ServiceException(exception.getMessage());
        }
    }
}
