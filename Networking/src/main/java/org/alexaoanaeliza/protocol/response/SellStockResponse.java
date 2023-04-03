package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.Sale;

public class SellStockResponse implements Response {
    private final Sale sale;

    public SellStockResponse(Sale sale) {
        this.sale = sale;
    }

    public Sale getSale() {
        return sale;
    }
}
