package com.infor.assignment.carrentalservice.model.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infor.assignment.carrentalservice.annotation.Brand;
import com.infor.assignment.carrentalservice.util.VehicleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class Car extends Vehicle {

    @JsonProperty("carBodyConfiguration")
    private CarBodyConfiguration carBodyConfiguration;

    public Car() {
        this.setType(VehicleType.CAR);
    }

    public Car(String id, String plateId, VehicleType type, Brand brand, Fuel typeOfFuel, Transmission transmission, List<Feature> features, CarBodyConfiguration carBodyConfiguration) {
        super(id, plateId, type, brand, typeOfFuel, transmission, features);
        this.carBodyConfiguration = carBodyConfiguration;
    }
}
