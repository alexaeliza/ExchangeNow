package org.alexaoanaeliza.protocol.request;

import java.time.LocalDate;

public class GetStockPredictionsRequest implements Request {
    private final String stockId;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public GetStockPredictionsRequest(String stockId, LocalDate startDate, LocalDate endDate) {
        this.stockId = stockId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStockId() {
        return stockId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
