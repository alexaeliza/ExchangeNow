package org.alexaoanaeliza;

import java.time.LocalDateTime;

public class Purchase extends Transaction {
    protected Purchase(DebitCard debitCard, Stock stock, LocalDateTime dateTime, Double sum) {
        super(debitCard, stock, dateTime, sum);
        this.getDebitCard().withdrawAmount(sum);
    }
}
