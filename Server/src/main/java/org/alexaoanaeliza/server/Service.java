package org.alexaoanaeliza.server;

import org.alexaoanaeliza.*;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.exception.ServiceException;
import org.alexaoanaeliza.service.ServiceInterface;

import java.time.LocalDate;

public class Service implements ServiceInterface {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BankAccountRepository bankAccountRepository;
    private final DebitCardRepository debitCardRepository;
    private static Service service;

    private Service() {
        this.userRepository = UserRepository.getInstance();
        this.addressRepository = AddressRepository.getInstance();
        this.bankAccountRepository = BankAccountRepository.getInstance();
        this.debitCardRepository = DebitCardRepository.getInstance();
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
    public void addUser(String firstName, String lastName, String email, String password, String phoneNumber, String personalNumber, LocalDate birthday, Country country, String county, String city, String street, String number, String apartment, Integer floor) {
        User user = new User(firstName, lastName, personalNumber, new Address(country, county, city,
                street, number, apartment, floor), phoneNumber, birthday, email, password);
        userRepository.add(user);
    }
}
