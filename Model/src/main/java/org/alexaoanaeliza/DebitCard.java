package org.alexaoanaeliza;

import org.alexaoanaeliza.enums.DebitCardType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class DebitCard extends Entity<Long> {
    private final BankAccount bankAccount;
    private final DebitCardType debitCardType;
    private final String cardNumber;
    private final String cvv;
    private final LocalDate expireDate;
    private final User owner;

    public DebitCard() {
        super(0L);
        this.bankAccount = null;
        this.debitCardType = DebitCardType.MAESTRO;
        this.cardNumber = "";
        this.cvv = "";
        this.expireDate = LocalDate.now();
        this.owner = null;
    }

    public DebitCard(Long id, BankAccount bankAccount, DebitCardType debitCardType, String cardNumber, String cvv, LocalDate expireDate, User owner) {
        super(id);
        this.bankAccount = bankAccount;
        this.debitCardType = debitCardType;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.owner = owner;
        this.owner.addDebitCard(this);
    }

    public BankAccount getBankAccount() {
        return bankAccount;
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

    public DebitCardType getDebitCardType() {
        return debitCardType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (super.equals(o)) return true;
        DebitCard debitCard = (DebitCard) o;
        return debitCardType == debitCard.debitCardType && cardNumber.equals(debitCard.cardNumber) && cvv.equals(debitCard.cvv) && expireDate.equals(debitCard.expireDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debitCardType, cardNumber, cvv, expireDate);
    }
}
