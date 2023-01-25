package org.alexaoanaeliza.server;

import org.alexaoanaeliza.*;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.enums.DebitCardType;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.ServiceException;
import org.alexaoanaeliza.service.ServiceInterface;


import java.time.LocalDate;

public class Service implements ServiceInterface {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final DebitCardRepository debitCardRepository;
    private final VirtualAccountRepository virtualAccountRepository;
    private final StockRepository stockRepository;
    private static Service service;

    private Service() {
        this.userRepository = UserRepository.getInstance();
        this.addressRepository = AddressRepository.getInstance();
        this.debitCardRepository = DebitCardRepository.getInstance();
        this.virtualAccountRepository = VirtualAccountRepository.getInstance();
        this.stockRepository = StockRepository.getInstance();
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
        User user = new User(firstName, lastName, personalNumber, new Address(country, county, city,
                street, number, apartment), phoneNumber, birthday, email, password);
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
            debitCard.withdrawAmount(amount);
            debitCard.getOwner().getVirtualAccount().depositAmount(amount);
            virtualAccountRepository.update(debitCard.getOwner().getVirtualAccount());
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
            DebitCard debitCard = new DebitCard(debitCardType, cardNumber, cvv, expireDate, owner);
            return debitCardRepository.add(debitCard);
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }
}
