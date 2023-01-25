package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.DebitCard;

public class GetDebitCardByDataResponse implements Response {
    private final DebitCard debitCard;

    public GetDebitCardByDataResponse(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }
}
