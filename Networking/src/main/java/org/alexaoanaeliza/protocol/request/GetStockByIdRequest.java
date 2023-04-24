package org.alexaoanaeliza.protocol.request;

public class GetStockByIdRequest implements Request {
    private final Long stockId;

    public GetStockByIdRequest(Long stockId) {
        this.stockId = stockId;
    }

    public Long getStockId() {
        return stockId;
    }
}
