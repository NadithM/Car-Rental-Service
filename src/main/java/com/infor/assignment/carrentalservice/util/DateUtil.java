package com.infor.assignment.carrentalservice.util;

import com.infor.assignment.carrentalservice.model.common.DateRange;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateUtil {

    private static boolean isOverlapping(LocalDateTime vehicleNotAvailableFrom, LocalDateTime vehicleNotAvailableTo, LocalDateTime expectedFrom, LocalDateTime expectedTo) {
        return vehicleNotAvailableFrom.isBefore(expectedTo) && expectedFrom.isBefore(vehicleNotAvailableTo);
    }

    public boolean isOverlapping(DateRange vehicleNotAvailable, DateRange expected) {
        return isOverlapping(vehicleNotAvailable.getFrom(), vehicleNotAvailable.getTo(), expected.getFrom(), expected.getTo());
    }

    private static boolean isWithinRange(DateRange available, LocalDateTime testDate) {
        return !(testDate.isBefore(available.getFrom()) || testDate.isAfter(available.getTo()));
    }

    public boolean isWithinRange(DateRange vehicleAvailable, DateRange expected) {
        return isWithinRange(vehicleAvailable, expected.getFrom()) && isWithinRange(vehicleAvailable, expected.getTo());
    }
}
