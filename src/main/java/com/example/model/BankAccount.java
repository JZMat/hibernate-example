package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int account_id;
    private String account_name;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @ManyToMany
    @JoinTable(
            name = "owner_account",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
    private Set<Owner> owners = new HashSet<>();

    @OneToMany(mappedBy = "sourceAccount")
    private Set<Transaction> sourceTransactions = new HashSet<>();

    @OneToMany(mappedBy = "destinationAccount")
    private Set<Transaction> destinationTransactions = new HashSet<>();

    public BankAccount(String account_name, BigDecimal balance, Bank bank) {
        this.account_name = account_name;
        this.balance = balance;
        this.bank = bank;
    }

    public void addOwner(Owner owner) {
        this.owners.add(owner);
    }

    public void removeOwner(Owner owner) {
        this.owners.remove(owner);
    }

    public int getAccountId() {
        return account_id;
    }
}
