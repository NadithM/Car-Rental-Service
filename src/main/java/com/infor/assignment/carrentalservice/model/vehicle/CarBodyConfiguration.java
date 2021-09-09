package com.infor.assignment.carrentalservice.model.vehicle;

public enum CarBodyConfiguration {
    HATCH_BACK("HATCH_BACK"),
    SEDAN("SEDAN"),
    SUV("SUV");

    private String value;

    CarBodyConfiguration(String value) {
        this.value = value;
    }
}
