package org.alexaoanaeliza.protocol.request;

import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.DebitCardType;

import java.time.LocalDate;

public class AddDebitCardRequest implements Request {
    private final DebitCardType debitCardType;
    private final String cardNumber;
    private final String cvv;
    private final LocalDate expireDate;
    private final User owner;

    public AddDebitCardRequest(DebitCardType debitCardType, String cardNumber, String cvv, LocalDate expireDate, User owner) {
        this.debitCardType = debitCardType;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.owner = owner;
    }

    public DebitCardType getDebitCardType() {
        return debitCardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public User getOwner() {
        return owner;
    }
}
