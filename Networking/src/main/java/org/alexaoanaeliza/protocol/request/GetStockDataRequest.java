package org.alexaoanaeliza.protocol.request;

public class GetStockDataRequest implements Request {
    private final String stockId;

    public GetStockDataRequest(String stockId) {
        this.stockId = stockId;
    }

    public String getStockId() {
        return stockId;
    }
}
