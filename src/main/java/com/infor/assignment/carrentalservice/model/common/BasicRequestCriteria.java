package com.infor.assignment.carrentalservice.model.common;

import com.infor.assignment.carrentalservice.model.vehicle.KeyControls;
import com.infor.assignment.carrentalservice.util.MessageType;
import com.infor.assignment.carrentalservice.util.VehicleType;
import lombok.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BasicRequestCriteria<T extends KeyControls> {
    private String requestId;
    private MessageType messageType;
    private DeferredResult deferredResult;
    private VehicleType vehicleType;
    private String plateId;
    private String id;
    private T keyControls;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicRequestCriteria that = (BasicRequestCriteria) o;
        return Objects.equals(requestId, that.requestId) && messageType == that.messageType && Objects.equals(deferredResult, that.deferredResult) && Objects.equals(plateId, that.plateId) && Objects.equals(id, that.id) && Objects.equals(keyControls, that.keyControls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, messageType, deferredResult, plateId, id, keyControls);
    }
}
