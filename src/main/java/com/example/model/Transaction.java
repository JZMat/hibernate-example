package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transaction_id;
    private BigDecimal amount;
    private LocalDateTime transaction_date;

    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private BankAccount sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private BankAccount destinationAccount;

    public Transaction(BigDecimal amount, LocalDateTime transaction_date, BankAccount sourceAccount, BankAccount destinationAccount) {
        this.amount = amount;
        this.transaction_date = transaction_date;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }

    public void setTransactionDate(LocalDateTime transaction_date) {
        this.transaction_date = transaction_date;
    }
}
