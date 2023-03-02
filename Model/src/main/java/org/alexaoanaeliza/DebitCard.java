package org.alexaoanaeliza;

import org.alexaoanaeliza.enums.DebitCardType;

import java.time.LocalDate;
import java.util.Objects;

public class DebitCard extends Entity<Long> {
    private final DebitCardType debitCardType;
    private final String cardNumber;
    private final String cvv;
    private final LocalDate expireDate;
    private final Long ownerId;

    public DebitCard() {
        super(0L);
        debitCardType = DebitCardType.MAESTRO;
        cardNumber = "";
        cvv = "";
        expireDate = LocalDate.now();
        ownerId = null;
    }

    public DebitCard(Long id, DebitCardType debitCardType, String cardNumber, String cvv, LocalDate expireDate, Long ownerId) {
        super(id);
        this.debitCardType = debitCardType;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.ownerId = ownerId;
    }

    public DebitCard(DebitCardType debitCardType, String cardNumber, String cvv, LocalDate expireDate, Long ownerId) {
        super(0L);
        this.debitCardType = debitCardType;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.ownerId = ownerId;
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

    public Long getOwnerId() {
        return ownerId;
    }

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
