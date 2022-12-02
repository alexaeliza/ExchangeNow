package org.alexaoanaeliza;

import org.alexaoanaeliza.enums.Currency;

import java.util.Objects;

public class BankAccount extends Account {
    private Double sold;
    private final String iban;
    private final Currency currency;

    public BankAccount() {
        super(0L, null);
        this.iban = "";
        this.currency = Currency.EUR;
        this.sold = 0D;
    }

    public BankAccount(Long id, String iban, Currency currency, User owner) {
        super(id, owner);
        this.iban = iban;
        this.currency = currency;
        this.sold = 0D;
        this.getOwner().addBankAccount(this);
    }

    public String getIban() {
        return iban;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void addSale(Sale sale) {
        depositAmount(sale.getSum());
    }

    protected void depositAmount(Double amount) {
        if (amount <= 0D)
            throw new NumberFormatException("Deposit amount should be greater than 0");
        if (amount >= 100000D)
            throw new NumberFormatException("Deposit amount should be lower than 100000");
        this.sold += amount;
    }

    protected void withdrawAmount(Double amount) {
        if (amount <= 0D)
            throw new NumberFormatException("Withdraw amount should be greater than 0");
        if (amount >= 100000D)
            throw new NumberFormatException("Withdraw amount should be lower than 100000");
        if (amount > this.sold)
            throw new NumberFormatException("Withdraw amount is greater than your sold");
        this.sold -= amount;
    }

    public void addPurchase(Purchase purchase) {
        withdrawAmount(purchase.getSum());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (super.equals(o)) return true;
        BankAccount that = (BankAccount) o;
        return iban.equals(that.iban) && currency == that.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban, currency);
    }

    public Double getSold() {
        return sold;
    }
}
