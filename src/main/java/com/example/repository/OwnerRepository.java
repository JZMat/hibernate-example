package com.example.repository;

import com.example.model.Owner;

import java.util.List;

public interface OwnerRepository {
    void saveOwner(Owner owner);

    List<Owner> getAllOwners();
}
