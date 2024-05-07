package com.example.service;

import com.example.model.Owner;

import java.util.List;

public interface OwnerService {
    void saveOwner(Owner owner);

    // Method to list all owners
    List<Owner> getAllOwners();
}
