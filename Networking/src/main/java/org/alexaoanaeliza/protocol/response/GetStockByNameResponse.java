package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.Stock;

public class GetStockByNameResponse implements Response {
    private final Stock stock;

    public GetStockByNameResponse(Stock stock) {
        this.stock = stock;
    }

    public Stock getStock() {
        return stock;
    }
}
