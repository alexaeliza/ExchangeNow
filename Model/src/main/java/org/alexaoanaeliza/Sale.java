package org.alexaoanaeliza;

import java.time.LocalDateTime;

public class Sale extends Transaction {
    protected Sale(DebitCard debitCard, Stock stock, LocalDateTime dateTime, Double sum) {
        super(debitCard, stock, dateTime, sum);
        this.getDebitCard().depositAmount(sum);
    }
}
