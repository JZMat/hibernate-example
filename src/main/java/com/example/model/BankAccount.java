package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class
BankAccount {
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

    public BankAccount(String account_name, BigDecimal balance, Bank bank) {
        this.account_name = account_name;
        this.balance = balance;
        this.bank = bank;
    }

    public void addOwner(Owner owner) {

        this.owners.add(owner);
    }
}

