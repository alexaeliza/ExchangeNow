package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.Purchase;

public class BuyStockResponse implements Response {
    private final Purchase purchase;

    public BuyStockResponse(Purchase purchase) {
        this.purchase = purchase;
    }

    public Purchase getPurchase() {
        return purchase;
    }
}
