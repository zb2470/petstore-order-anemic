package com.thoughtworks.ddd.order.infrastructure.client;

public class PetStatusChanged {
    public String petId;
    public PetStatus petStatus;

    public PetStatusChanged() {
    }

    public PetStatusChanged(String petId, PetStatus locked) {
    }
}
