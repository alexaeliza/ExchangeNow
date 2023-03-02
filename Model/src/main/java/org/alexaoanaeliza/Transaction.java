package org.alexaoanaeliza;

import java.time.LocalDateTime;

public abstract class Transaction extends Entity<Long> {
    private final Long userId;
    private final Long stockId;
    private final LocalDateTime dateTime;
    private final Double sum;

    public Transaction(Long userId, Long stockId, LocalDateTime dateTime, Double sum) {
        super(0L);
        this.userId = userId;
        this.stockId = stockId;
        this.dateTime = dateTime;
        this.sum = sum;
    }

    protected Transaction(Long id, Long userId, Long stockId, LocalDateTime dateTime, Double sum) {
        super(id);
        this.userId = userId;
        this.stockId = stockId;
        this.dateTime = dateTime;
        this.sum = sum;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getStockId() {
        return stockId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Double getSum() {
        return sum;
    }
}
