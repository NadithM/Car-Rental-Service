package com.infor.assignment.carrentalservice.model.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infor.assignment.carrentalservice.annotation.CarBodyConfigType;
import lombok.*;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CarRegisterRequest extends VehicleRegisterRequest {

    @CarBodyConfigType
    @JsonProperty(value = "carBodyConfiguration")
    private String carBodyConfiguration;

    public CarRegisterRequest(String plateNumber, String brand, String typeOfFuel, String transmission, List<String> features, String carBodyConfiguration) {
        super(plateNumber, brand, typeOfFuel, transmission, features);
        this.carBodyConfiguration = carBodyConfiguration;
    }
}
