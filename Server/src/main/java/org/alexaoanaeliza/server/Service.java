package org.alexaoanaeliza.server;

import org.alexaoanaeliza.*;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.ServiceException;
import org.alexaoanaeliza.service.ServiceInterface;


import java.time.LocalDate;

public class Service implements ServiceInterface {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BankAccountRepository bankAccountRepository;
    private final DebitCardRepository debitCardRepository;
    private final VirtualAccountRepository virtualAccountRepository;
    private static Service service;

    private Service() {
        this.userRepository = UserRepository.getInstance();
        this.addressRepository = AddressRepository.getInstance();
        this.bankAccountRepository = BankAccountRepository.getInstance();
        this.debitCardRepository = DebitCardRepository.getInstance();
        this.virtualAccountRepository = VirtualAccountRepository.getInstance();
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
    public void depositAmount(Double amount, User user) {
        try {
            user.getVirtualAccount().depositAmount(amount);
            virtualAccountRepository.update(user.getVirtualAccount());
        } catch (DatabaseException databaseException) {
            throw new ServiceException(databaseException.getMessage());
        }
    }
}
