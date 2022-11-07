package org.alexaoanaeliza;

public abstract class Account extends Entity<Long> {
    private final User owner;

    public Account(User owner) {
        super(0L);
        this.owner = owner;
    }

    protected Account(Long id, User owner) {
        super(id);
        this.owner = owner;
    }

    protected abstract void depositAmount(Double amount);

    protected abstract void withdrawAmount(Double amount);

    public abstract void addSale(Sale sale);
    public abstract void addPurchase(Purchase purchase);

    public User getOwner() {
        return owner;
    }
}
