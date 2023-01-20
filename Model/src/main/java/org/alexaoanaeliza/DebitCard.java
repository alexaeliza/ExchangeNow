package org.alexaoanaeliza;

import org.alexaoanaeliza.enums.DebitCardType;

import java.time.LocalDate;
import java.util.Objects;

public class DebitCard extends Account {
    private final DebitCardType debitCardType;
    private final String cardNumber;
    private final String cvv;
    private final LocalDate expireDate;
    private Double sold;

    public DebitCard() {
        super(0L, null);
        this.debitCardType = DebitCardType.MAESTRO;
        this.cardNumber = "";
        this.cvv = "";
        this.expireDate = LocalDate.now();
        this.sold = 0D;
    }

    public DebitCard(Long id, DebitCardType debitCardType, String cardNumber, String cvv, LocalDate expireDate, User owner) {
        super(id, owner);
        this.debitCardType = debitCardType;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.sold = Double.MAX_VALUE;
        this.getOwner().addDebitCard(this);
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

    @Override
    public void withdrawAmount(Double sum) {
        this.sold -= sum;
    }

    @Override
    public void depositAmount(Double sum) {
        this.sold += sum;
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
