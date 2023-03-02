package org.alexaoanaeliza.asbtractRepository;

import org.alexaoanaeliza.Stock;

import java.time.LocalDate;
import java.util.HashMap;

public interface StockRepositoryInterface extends RepositoryInterface<Long, Stock> {
    Stock getStockBySale(Long saleId);
    Stock getStockByPurchase(Long purchaseId);
    Double getStockPriceByDate(Long stockId, LocalDate localDate);
}
