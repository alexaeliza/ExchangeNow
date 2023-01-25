package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.DebitCard;

public class GetDebitCardByIdResponse implements Response {
    private final DebitCard debitCard;

    public GetDebitCardByIdResponse(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }
}
