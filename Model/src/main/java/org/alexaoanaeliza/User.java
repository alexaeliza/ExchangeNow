package org.alexaoanaeliza;

import com.sun.jdi.request.DuplicateRequestException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class User extends Entity<Long> implements Serializable {
    private String firstName;
    private String lastName;
    private final String personalNumber;
    private Address address;
    private String phoneNumber;
    private final LocalDate birthday;
    private final String email;
    private final String password;
    private final Set<BankAccount> bankAccounts;
    private final Set<DebitCard> debitCards;

    public User() {
        super(0L);
        personalNumber = "";
        birthday = LocalDate.now();
        email = "";
        password = "";
        bankAccounts = new HashSet<>();
        debitCards = new HashSet<>();
    }

    public User(String firstName, String lastName, String personalNumber, Address address, String phoneNumber, LocalDate birthday, String email, String password) {
        super(0L);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.bankAccounts = new HashSet<>();
        this.debitCards = new HashSet<>();
        this.address.addTenant(this);
    }

    public User(Long id, String firstName, String lastName, String personalNumber, Address address, String phoneNumber, LocalDate birthday, String email, String password) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.bankAccounts = new HashSet<>();
        this.debitCards = new HashSet<>();
        this.address.addTenant(this);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Set<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public Set<DebitCard> getDebitCards() {
        return debitCards;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(Address address) {
        this.address.removeTenant(this);
        this.address = address;
        this.address.addTenant(this);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    protected void addBankAccount(BankAccount bankAccount) {
        if (bankAccounts.contains(bankAccount))
            throw new DuplicateRequestException("This bank account is already registered for this user");
        bankAccounts.add(bankAccount);
    }

    protected void addDebitCard(DebitCard debitCard) {
        if (!debitCards.contains(debitCard))
            throw new DuplicateRequestException("This debit card is already registered for this user");
        debitCards.add(debitCard);
    }
}
