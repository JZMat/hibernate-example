package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int owner_id;

    @Column(unique = true)
    private String owner_name;

    public Owner(String ownerName){
        this.owner_name = ownerName;
    }
}
