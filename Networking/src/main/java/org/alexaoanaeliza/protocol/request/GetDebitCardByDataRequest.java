package org.alexaoanaeliza.protocol.request;

import org.alexaoanaeliza.enums.DebitCardType;

import java.time.LocalDate;

public class GetDebitCardByDataRequest implements Request {
    private final String cardNumber;
    private final String cvv;
    private final LocalDate expireDate;
    private final DebitCardType debitCardType;

    public GetDebitCardByDataRequest(String cardNumber, String cvv, LocalDate expireDate, DebitCardType debitCardType) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.debitCardType = debitCardType;
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

    public DebitCardType getDebitCardType() {
        return debitCardType;
    }
}
