package org.alexaoanaeliza;

import org.alexaoanaeliza.enums.Bank;
import org.alexaoanaeliza.enums.Currency;

import java.io.Serializable;
import java.util.Objects;

public class BankAccount extends Entity<Long> implements Serializable {
    private final String iban;
    private final Currency currency;
    private Double sold;
    private final Bank bank;
    private final User owner;

    public BankAccount() {
        super(0L);
        this.iban = "";
        this.currency = Currency.EUR;
        this.bank = Bank.BCR;
        this.owner = null;
        this.sold = 0D;
    }

    public BankAccount(Long id, String iban, Currency currency, Bank bank, User owner) {
        super(id);
        this.iban = iban;
        this.currency = currency;
        this.bank = bank;
        this.owner = owner;
        this.sold = 0D;
        this.owner.addBankAccount(this);
    }

    public String getIban() {
        return iban;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Double getSold() {
        return sold;
    }

    public Bank getBank() {
        return bank;
    }

    public User getOwner() {
        return owner;
    }

    public void depositAmount(Double amount) {
        if (amount <= 0D)
            throw new NumberFormatException("Deposit amount should be greater than 0");
        if (amount >= 100000D)
            throw new NumberFormatException("Deposit amount should be lower than 100000");
        this.sold += amount;
    }

    public void withdrawAmount(Double amount) {
        if (amount <= 0D)
            throw new NumberFormatException("Withdraw amount should be greater than 0");
        if (amount >= 100000D)
            throw new NumberFormatException("Withdraw amount should be lower than 100000");
        if (amount > this.sold)
            throw new NumberFormatException("Withdraw amount is greater than your sold");
        this.sold -= amount;
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
