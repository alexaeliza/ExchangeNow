package org.alexaoanaeliza;

import java.util.Set;

public class VirtualAccount extends Entity<Long> {
    private final User owner;
    private Double sold;
    private Set<Sale> sales;
    private Set<Purchase> purchases;

    protected VirtualAccount(Long id, User owner) {
        super(id);
        this.owner = owner;
    }

    private void depositAmount(Double amount) {
        if (amount <= 0D)
            throw new NumberFormatException("Deposit amount should be greater than 0");
        if (amount >= 100000D)
            throw new NumberFormatException("Deposit amount should be lower than 100000");
        this.sold += amount;
    }

    private void withdrawAmount(Double amount) {
        if (amount <= 0D)
            throw new NumberFormatException("Withdraw amount should be greater than 0");
        if (amount >= 100000D)
            throw new NumberFormatException("Withdraw amount should be lower than 100000");
        if (amount > this.sold)
            throw new NumberFormatException("Withdraw amount is greater than your sold");
        this.sold -= amount;
    }

    public void addSale(Sale sale) {
        this.sales.add(sale);
        depositAmount(sale.getSum());
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
        withdrawAmount(purchase.getSum());
    }

    public User getOwner() {
        return owner;
    }

    public Double getSold() {
        return sold;
    }

    public Set<Sale> getSales() {
        return sales;
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }
}
