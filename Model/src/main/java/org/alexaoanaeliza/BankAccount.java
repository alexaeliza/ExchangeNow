package org.alexaoanaeliza;

import org.alexaoanaeliza.enums.Bank;
import org.alexaoanaeliza.enums.Currency;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BankAccount extends Entity<Long> {
    private final String iban;
    private final Currency currency;
    private final Bank bank;
    private Double sold;
    private final User owner;

    public BankAccount() {
        super(0L);
        this.owner = null;
        this.iban = "";
        this.currency = Currency.EUR;
        this.bank = Bank.BCR;
    }

    public BankAccount(Long id, String iban, Currency currency, Bank bank, User owner) {
        super(id);
        this.iban = iban;
        this.currency = currency;
        this.bank = bank;
        this.owner = owner;
        this.owner.addBankAccount(this);
    }

    public String getIban() {
        return iban;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Bank getBank() {
        return bank;
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
        depositAmount(sale.getSum());
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
        return iban.equals(that.iban) && currency == that.currency && bank == that.bank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban, currency, bank);
    }
}
