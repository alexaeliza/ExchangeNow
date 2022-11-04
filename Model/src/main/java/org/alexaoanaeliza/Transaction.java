package org.alexaoanaeliza;

import java.time.LocalDateTime;

public abstract class Transaction extends Entity<Long> {
    private final BankAccount bankAccount;
    private final Stock stock;
    private final LocalDateTime dateTime;
    private final Double sum;

    public Transaction(BankAccount bankAccount, Stock stock, LocalDateTime dateTime, Double sum) {
        super(0L);
        this.bankAccount = bankAccount;
        this.stock = stock;
        this.dateTime = dateTime;
        this.sum = sum;
    }

    protected Transaction(Long id, BankAccount bankAccount, Stock stock, LocalDateTime dateTime, Double sum) {
        super(id);
        this.bankAccount = bankAccount;
        this.stock = stock;
        this.dateTime = dateTime;
        this.sum = sum;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
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
