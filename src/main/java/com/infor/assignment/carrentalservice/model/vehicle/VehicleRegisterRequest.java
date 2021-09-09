package com.infor.assignment.carrentalservice.model.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infor.assignment.carrentalservice.annotation.BrandType;
import com.infor.assignment.carrentalservice.annotation.FeatureType;
import com.infor.assignment.carrentalservice.annotation.FuelType;
import com.infor.assignment.carrentalservice.annotation.TransmissionType;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


// I Only validated this particular class fully using javax. this way we can do all request classes.

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VehicleRegisterRequest {

    @JsonProperty(value = "plateNumber")
    @NotEmpty(message = "Plate Number can't be null or empty")
    private String plateNumber;

    @BrandType
    @JsonProperty(value = "brand")
    private String brand;

    @FuelType
    @JsonProperty(value = "typeOfFuel")
    private String typeOfFuel;

    @TransmissionType
    @JsonProperty(value = "transmission")
    private String transmission;

    @FeatureType
    @JsonProperty(value = "features")
    private List<String> features;

    public VehicleRegisterRequest(String plateNumber, String brand, String typeOfFuel, String transmission, List<String> features) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.typeOfFuel = typeOfFuel;
        this.transmission = transmission;
        this.features = features;
    }
}
