package org.alexaoanaeliza;

import java.time.LocalDateTime;

public class Purchase extends Transaction {
    protected Purchase(Long userId, Long stockId, LocalDateTime dateTime, Double sum) {
        super(userId, stockId, dateTime, sum);
    }
}
