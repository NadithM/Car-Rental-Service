package com.infor.assignment.carrentalservice.exception;

public class VehicleNotFound extends ServiceException {
    private String id;
    private String plateId;

    public VehicleNotFound(String message, String id, String plateId) {
        super(message);
        this.id = id;
        this.plateId = plateId;
    }
}
