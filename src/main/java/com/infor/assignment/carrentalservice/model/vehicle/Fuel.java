package com.infor.assignment.carrentalservice.model.vehicle;

public enum Fuel {
    PETROL("Petrol"),
    DIESEL("Diesel"),
    LPG("LPG"),
    ELECTRIC("Electric"),
    HYBRID("Hybrid");

    private String value;

    Fuel(String value) {
        this.value = value;
    }


}
