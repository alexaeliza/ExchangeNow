package org.alexaoanaeliza;

import java.time.LocalDateTime;

public class Purchase extends Transaction {
    public Purchase(Long userId, Long stockId, LocalDateTime dateTime, Double sum, Double quantity) {
        super(userId, stockId, dateTime, sum, quantity);
    }
}
