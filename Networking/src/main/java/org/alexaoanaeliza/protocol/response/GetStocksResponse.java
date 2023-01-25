package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.Stock;

import java.util.Set;

public class GetStocksResponse implements Response {
    private final Set<Stock> stocks;

    public GetStocksResponse(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }
}
