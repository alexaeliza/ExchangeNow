package org.alexaoanaeliza.protocol.response;

import java.time.LocalDate;
import java.util.Map;

public class GetStockDataResponse implements Response {
    private final Map<LocalDate, Double> stockData;


    public GetStockDataResponse(Map<LocalDate, Double> stockData) {
        this.stockData = stockData;
    }

    public Map<LocalDate, Double> getStockData() {
        return stockData;
    }
}
