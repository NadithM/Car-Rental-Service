package com.infor.assignment.carrentalservice.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infor.assignment.carrentalservice.model.common.DateRange;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    @JsonProperty(value = "orderId")
    private String orderId;
    @JsonProperty(value = "userId")
    private String userId;
    @JsonProperty(value = "plateId")
    private String plateId;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "rentedPeriod")
    private DateRange rentedPeriod;
    @JsonProperty(value = "rentedRatePerHour")
    private Double rentedRatePerHour;
    @JsonProperty(value = "createdAt")
    private LocalDateTime createdAt;
    @JsonProperty(value = "modifiedAt")
    private LocalDateTime modifiedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return userId.equals(order.userId) && plateId.equals(order.plateId) && Objects.equals(rentedPeriod, order.rentedPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, plateId, rentedPeriod);
    }
}
