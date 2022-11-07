package org.alexaoanaeliza;

import java.util.Set;

public class VirtualAccount extends Account {
    private Double availableSold;
    private Double usedSold;
    private Set<Sale> sales;
    private Set<Purchase> purchases;

    public VirtualAccount(User owner) {
        super(0L, owner);
        this.availableSold = 0D;
        this.usedSold = 0D;
    }

    protected VirtualAccount(Long id, User owner) {
        super(id, owner);
        this.availableSold = 0D;
        this.usedSold = 0D;
    }

    protected void depositAmount(Double amount) {
        if (amount <= 0D)
            throw new NumberFormatException("Deposit amount should be greater than 0");
        if (amount >= 100000D)
            throw new NumberFormatException("Deposit amount should be lower than 100000");
        this.availableSold += amount;
    }

    protected void withdrawAmount(Double amount) {
        if (amount <= 0D)
            throw new NumberFormatException("Withdraw amount should be greater than 0");
        if (amount >= 100000D)
            throw new NumberFormatException("Withdraw amount should be lower than 100000");
        if (amount > this.availableSold)
            throw new NumberFormatException("Withdraw amount is greater than your sold");
        this.availableSold -= amount;
    }

    public void addSale(Sale sale) {
        this.sales.add(sale);
        this.usedSold -= sale.getSum();
        depositAmount(sale.getSum());
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
        this.usedSold += purchase.getSum();
        withdrawAmount(purchase.getSum());
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

    public Double getUsedSold() {
        return usedSold;
    }
}
