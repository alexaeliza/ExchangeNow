package org.alexaoanaeliza.protocol.request;

import org.alexaoanaeliza.DebitCard;

public class DepositAmountRequest implements Request {
    private final Double sum;
    private final DebitCard debitCard;

    public DepositAmountRequest(Double sum, DebitCard debitCard) {
        this.sum = sum;
        this.debitCard = debitCard;
    }

    public Double getSum() {
        return sum;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }
}
