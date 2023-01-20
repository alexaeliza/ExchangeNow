package org.alexaoanaeliza;

import java.time.LocalDateTime;

public abstract class Transaction extends Entity<Long> {
    private final DebitCard debitCard;
    private final Stock stock;
    private final LocalDateTime dateTime;
    private final Double sum;

    public Transaction(DebitCard debitCard, Stock stock, LocalDateTime dateTime, Double sum) {
        super(0L);
        this.debitCard = debitCard;
        this.stock = stock;
        this.dateTime = dateTime;
        this.sum = sum;
    }

    protected Transaction(Long id, DebitCard debitCard, Stock stock, LocalDateTime dateTime, Double sum) {
        super(id);
        this.debitCard = debitCard;
        this.stock = stock;
        this.dateTime = dateTime;
        this.sum = sum;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public Stock getStock() {
        return stock;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Double getSum() {
        return sum;
    }
}
