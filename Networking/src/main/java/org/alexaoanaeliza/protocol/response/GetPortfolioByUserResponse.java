package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.Stock;

import java.util.Map;

public class GetPortfolioByUserResponse implements Response {
    private final Map<Stock, Double> portfolio;

    public GetPortfolioByUserResponse(Map<Stock, Double> portfolio) {
        this.portfolio = portfolio;
    }

    public Map<Stock, Double> getPortfolio() {
        return portfolio;
    }
}
