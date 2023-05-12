package org.alexaoanaeliza;

import java.time.LocalDateTime;

public class Sale extends Transaction {
    public Sale(Long userId, Long stockId, LocalDateTime dateTime, Double sum, Double quantity) {
        super(userId, stockId, dateTime, sum, quantity);
    }
}
