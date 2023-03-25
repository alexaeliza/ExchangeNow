package org.alexaoanaeliza.service;


import org.alexaoanaeliza.DebitCard;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.enums.DebitCardType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ServiceInterface {
    User loginUser(String email, String password);
    void addUser(String firstName, String lastName, String email, String password, String phoneNumber,
                 String personalNumber, LocalDate birthday, Country country, String county,
                 String city, String street, String number, String apartment);
    User getUserByEmail(String email);
    void depositAmount(Double amount, DebitCard debitCard);
    void withdrawAmount(Double amount, DebitCard debitCard);
    DebitCard getDebitCardById(Long id);
    DebitCard getDebitCardByData(String cardNumber, String cvv, LocalDate expireDate, DebitCardType debitCardType);
    DebitCard addDebitCard(String cardNumber, String cvv, LocalDate expireDate, DebitCardType debitCardType, User owner);
    Set<Stock> getStocks();
    Double getTodaySoldByUser(User user);
    Double getReturnValueByUser(User user);
    Double getReturnPercentageByUser(User user);
    Map<LocalDate, Double> getStockData(String stockId);
}