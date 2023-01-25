package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.DebitCard;

public class AddDebitCardResponse implements Response {
    private final DebitCard debitCard;

    public AddDebitCardResponse(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }
}
