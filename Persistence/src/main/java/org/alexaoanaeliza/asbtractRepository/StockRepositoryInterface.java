package org.alexaoanaeliza.asbtractRepository;

import org.alexaoanaeliza.Stock;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public interface StockRepositoryInterface extends RepositoryInterface<Long, Stock> {
    Stock getStockBySale(Long saleId);
    Stock getStockByPurchase(Long purchaseId);
    Double getStockPriceByDate(Long stockId, LocalDate localDate);
    Stock getStockByName(String name);
    void addPricesByStock(Map<LocalDate, Double> prices, Long stockId);
    LocalDate getLastStockPriceByName(String stockName);
    Map<LocalDate, Double> getStockPrices(String stockName);
}
