package com.infor.assignment.carrentalservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class DuplicateVehicleFoundException extends RuntimeException {

    public DuplicateVehicleFoundException(String message) {
        super(message);
    }
}
