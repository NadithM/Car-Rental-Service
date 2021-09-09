package com.infor.assignment.carrentalservice.model.vehicle;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infor.assignment.carrentalservice.annotation.Brand;
import com.infor.assignment.carrentalservice.model.common.DateRange;
import com.infor.assignment.carrentalservice.model.vehicle.Feature;
import com.infor.assignment.carrentalservice.model.vehicle.Fuel;
import com.infor.assignment.carrentalservice.model.vehicle.Transmission;
import com.infor.assignment.carrentalservice.util.VehicleType;
import lombok.*;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vehicle {

    @JsonProperty("id")
    private String id;
    @JsonProperty("plateId")
    private String plateId;
    @JsonProperty("type")
    private VehicleType type;
    @JsonProperty("brand")
    private Brand brand;
    @JsonProperty("typeOfFuel")
    private Fuel typeOfFuel;
    @JsonProperty("transmission")
    private Transmission transmission;
    @JsonProperty("features")
    private List<Feature> features;

    @JsonProperty("availableDates")
    private List<DateRange> availableDates;
    @JsonProperty("blackOutDates")
    private Map<String, DateRange> blackOutDatesByOrder;
    @JsonProperty("rentalPricePerHour")
    private Double rentalPricePerHour;
    @JsonProperty("active")
    private boolean active;

    public Vehicle(String id, String plateId, VehicleType type, Brand brand, Fuel typeOfFuel, Transmission transmission, List<Feature> features) {
        this.id = id;
        this.plateId = plateId;
        this.type = type;
        this.brand = brand;
        this.typeOfFuel = typeOfFuel;
        this.transmission = transmission;
        this.features = features;
    }
}
