package com.infor.assignment.carrentalservice.model.vehicle;

import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CarRequestCriteria extends BasicRequestCriteria<CarKeyControls> {
    private CarRegisterRequest carRegisterRequest;
    private CarAvailabilityRequest carAvailabilityRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CarRequestCriteria that = (CarRequestCriteria) o;
        return Objects.equals(carRegisterRequest, that.carRegisterRequest) && Objects.equals(carAvailabilityRequest, that.carAvailabilityRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), carRegisterRequest, carAvailabilityRequest);
    }
}
