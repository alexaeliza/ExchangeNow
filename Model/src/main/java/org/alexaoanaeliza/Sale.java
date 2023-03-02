package org.alexaoanaeliza;

import java.time.LocalDateTime;

public class Sale extends Transaction {
    protected Sale(Long userId, Long stockId, LocalDateTime dateTime, Double sum) {
        super(userId, stockId, dateTime, sum);
    }
}
