package org.alexaoanaeliza.protocol.request;

public class GetStockByNameRequest implements Request {
    private final String stockName;

    public GetStockByNameRequest(String stockName) {
        this.stockName = stockName;
    }

    public String getStockName() {
        return stockName;
    }
}
