package com.infor.assignment.carrentalservice.exception;

import com.infor.assignment.carrentalservice.model.common.DateRange;
import lombok.Data;

@Data
public class NoAvailabilityException extends RuntimeException {

    private String userId;
    private String plateId;
    private DateRange dateRange;

    public NoAvailabilityException(String message, String userId, String plateId, DateRange dateRange) {
        super(message);
        this.userId = userId;
        this.plateId = plateId;
        this.dateRange = dateRange;
    }
}
