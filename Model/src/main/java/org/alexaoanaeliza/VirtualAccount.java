package org.alexaoanaeliza;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class VirtualAccount extends Account {
    private Double investedAmount;
    private Double availableSold;
    private Double usedSold;
    private Set<Sale> sales;
    private Set<Purchase> purchases;

    public VirtualAccount(User owner) {
        super(0L, owner);
        this.investedAmount = 0D;
        this.availableSold = 0D;
        this.usedSold = 0D;
        this.sales = new HashSet<>();
        this.purchases = new HashSet<>();
    }

    protected VirtualAccount(Long id, User owner) {
        super(id, owner);
        this.investedAmount = 0D;
        this.availableSold = 0D;
        this.usedSold = 0D;
        this.sales = new HashSet<>();
        this.purchases = new HashSet<>();
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
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
        this.usedSold += purchase.getSum();
        this.availableSold -= purchase.getSum();
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
}
