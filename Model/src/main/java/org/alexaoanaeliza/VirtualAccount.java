package org.alexaoanaeliza;

import jdk.jfr.Percentage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class VirtualAccount extends Account {
    private Double investedAmount;
    private Double availableSold;
    private Double usedSold;
    private final Set<Sale> sales;
    private final Set<Purchase> purchases;
    private final Map<Stock, Double> stocks;

    public VirtualAccount(User owner) {
        super(0L, owner);
        this.investedAmount = 0D;
        this.availableSold = 0D;
        this.usedSold = 0D;
        this.sales = new HashSet<>();
        this.purchases = new HashSet<>();
        this.stocks = new HashMap<>();
    }

    protected VirtualAccount(Long id, User owner) {
        super(id, owner);
        this.investedAmount = 0D;
        this.availableSold = 0D;
        this.usedSold = 0D;
        this.sales = new HashSet<>();
        this.purchases = new HashSet<>();
        this.stocks = new HashMap<>();
    }

    public void depositAmount(Double amount) {
        this.investedAmount += amount;
    }

    public void withdrawAmount(Double amount) {
        this.investedAmount -= amount;
    }

    public void addSale(Sale sale) {
        this.sales.add(sale);
        this.usedSold -= sale.getSum();
        this.availableSold += sale.getSum();
        stocks.put(sale.getStock(), stocks.get(sale.getStock()) -
                sale.getSum() / sale.getStock().getCurrentPrice());
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
        this.usedSold += purchase.getSum();
        this.availableSold -= purchase.getSum();
        stocks.put(purchase.getStock(), stocks.get(purchase.getStock()) +
                purchase.getSum() / purchase.getStock().getCurrentPrice());
    }

    public Set<Sale> getSales() {
        return sales;
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public Double getSold() {
        return availableSold + usedSold;
    }

    public Double getAvailableSold() {
        return availableSold;
    }

    public Double getInvestedAmount() {
        return investedAmount;
    }

    public Double getUsedSold() {
        return usedSold;
    }

    public Double getTodaySold() {
        AtomicReference<Double> sold = new AtomicReference<>(0D);
        purchases.forEach(purchase -> sold.updateAndGet(v -> v + purchase.getStock().getCurrentPrice()));
        return sold.get() + availableSold;
    }

    public Double getReturnValue() {
        return investedAmount - getTodaySold();
    }

    public Double getReturnPercentage() {
        if (investedAmount.equals(0D))
            return 0D;
        return Math.abs(getReturnValue()) / investedAmount;
    }

    public Map<Stock, Double> getStocks() {
        return stocks;
    }
}
