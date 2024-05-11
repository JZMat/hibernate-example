package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bank_id;

    @Column(unique = true)
    private String bank_name;

    public Bank(String bankName) {
        this.bank_name = bankName;
    }
}
