package org.alexaoanaeliza;

import java.time.LocalDateTime;

public class Purchase extends Transaction {
    protected Purchase(BankAccount bankAccount, Stock stock, LocalDateTime dateTime, Double sum) {
        super(bankAccount, stock, dateTime, sum);
        this.getBankAccount().addPurchase(this);
    }
}
