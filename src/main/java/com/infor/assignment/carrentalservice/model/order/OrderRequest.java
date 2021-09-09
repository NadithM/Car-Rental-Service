package com.infor.assignment.carrentalservice.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infor.assignment.carrentalservice.model.common.DateRange;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {

    @JsonProperty("userId")
    @NotEmpty(message = "User Id can't be null or empty")
    private String userId;
    @JsonProperty("plateId")
    @NotEmpty(message = "Plate Number can't be null or empty")
    private String plateId;
    @JsonProperty("rentedPeriod")
    private DateRange rentedPeriod;
    @JsonProperty("rentedRatePerHour")
    private Double rentedRatePerHour;
}
