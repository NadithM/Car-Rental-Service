package com.infor.assignment.carrentalservice.model.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infor.assignment.carrentalservice.model.common.DateRange;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VehicleAvailabilityRequest {

    @JsonProperty("plateId")
    @NotEmpty(message = "Plate Number can't be null or empty")
    private String plateId;
    @JsonProperty("rentalPricePerHour")
    private Double rentalPricePerHour;
    @JsonProperty(value = "availableDates")
    private List<DateRange> availableDates;
}
