package com.example.service;

import com.example.model.Owner;
import com.example.repository.OwnerRepository;

import java.util.List;

public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public void saveOwner(Owner owner){
        this.ownerRepository.saveOwner(owner);
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerRepository.getAllOwners();
    }

}
