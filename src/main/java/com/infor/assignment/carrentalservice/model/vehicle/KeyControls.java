package com.infor.assignment.carrentalservice.model.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infor.assignment.carrentalservice.model.common.DateRange;
import com.infor.assignment.carrentalservice.util.VehicleType;
import lombok.*;

import java.util.List;
import java.util.Objects;


//filter this Integer value is there to mention the operation weather search results should be filtering on (AND operation) or (OR operation). haven't used it.
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class KeyControls {

    @JsonProperty("type")
    private List<VehicleType> type;
    @JsonProperty("typeOperation")
    private int typeOperation;

    @JsonProperty("brand")
    private List<String> brand;
    @JsonProperty("brandOperation")
    private int brandOperation;

    @JsonProperty("typeOfFuel")
    private List<Fuel> typeOfFuel;
    @JsonProperty("typeOfFuelOperation")
    private int typeOfFuelOperation;

    @JsonProperty("transmission")
    private List<Transmission> transmission;
    @JsonProperty("transmissionOperation")
    private int transmissionOperation;

    @JsonProperty("features")
    private List<Feature> features;
    @JsonProperty("featuresOperation")
    private int featuresOperation;

    @JsonProperty("availableDateRanges")
    private List<DateRange> availableDateRanges;
    @JsonProperty("minimumRate")
    private Double minimumRate;
    @JsonProperty("maximumRate")
    private Double maximumRate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyControls that = (KeyControls) o;
        return Objects.equals(type, that.type) && Objects.equals(brand, that.brand) && Objects.equals(typeOfFuel, that.typeOfFuel) && Objects.equals(transmission, that.transmission) && Objects.equals(features, that.features) && Objects.equals(availableDateRanges, that.availableDateRanges) && Objects.equals(minimumRate, that.minimumRate) && Objects.equals(maximumRate, that.maximumRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, brand, typeOfFuel, transmission, features, availableDateRanges, minimumRate, maximumRate);
    }
}
