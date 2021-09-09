package com.infor.assignment.carrentalservice.model.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CarKeyControls extends KeyControls {

    @JsonProperty("carBodyConfigurations")
    List<CarBodyConfiguration> carBodyConfigurations;
    @JsonProperty("carBodyConfigurationsOperation")
    private int carBodyConfigurationsOperation;
}
