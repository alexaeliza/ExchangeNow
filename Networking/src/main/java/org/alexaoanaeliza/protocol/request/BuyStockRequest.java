package org.alexaoanaeliza.protocol.request;

import java.time.LocalDateTime;

public class BuyStockRequest implements Request {
    private final Long userId;
    private final Long stockId;
    private final Double sum;
    private final LocalDateTime dateTime;

    public BuyStockRequest(Long userId, Long stockId, Double sum, LocalDateTime dateTime) {
        this.userId = userId;
        this.stockId = stockId;
        this.sum = sum;
        this.dateTime = dateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getStockId() {
        return stockId;
    }

    public Double getSum() {
        return sum;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
