package org.alexaoanaeliza;

import java.time.LocalDateTime;

public class Sale extends Transaction {
    protected Sale(BankAccount bankAccount, Stock stock, LocalDateTime dateTime, Double sum) {
        super(bankAccount, stock, dateTime, sum);
        this.getBankAccount().addSale(this);
    }
}
