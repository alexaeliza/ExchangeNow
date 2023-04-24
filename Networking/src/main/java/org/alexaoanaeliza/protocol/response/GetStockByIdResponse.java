package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.Stock;

public class GetStockByIdResponse implements Response {
    private final Stock stock;

    public GetStockByIdResponse(Stock stock) {
        this.stock = stock;
    }

    public Stock getStock() {
        return stock;
    }
}
