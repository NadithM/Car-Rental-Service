package com.infor.assignment.carrentalservice.model.vehicle;

public enum Feature {
    CRUISE_CONTROL("Cruise Control"),
    GPS("GPS"),
    CHILD_SEAT("Child Seat"),
    ALL_WHEEL_DRIVE("All Wheel Drive"),
    PET_FRIENDLY("Pet Friendly");

    private String value;

    Feature(String value) {
        this.value = value;
    }
}
