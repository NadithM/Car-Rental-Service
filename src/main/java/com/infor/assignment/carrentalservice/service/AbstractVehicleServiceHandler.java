package com.infor.assignment.carrentalservice.service;

import com.infor.assignment.carrentalservice.handler.vehicle.CarVehicleHandler;
import com.infor.assignment.carrentalservice.handler.vehicle.BasicVehicleHandler;
import com.infor.assignment.carrentalservice.util.MessageType;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractVehicleServiceHandler {

    @Autowired
    private CarVehicleHandler carHandler;

    protected BasicVehicleHandler getHandler(MessageType messageType) {
        switch (messageType) {
            case REGISTER_CAR:
            case GET_CAR:
            case SEARCH_AVAILABLE_CARS:
            case REGISTER_AVAILABILITY_CAR:
                return carHandler;

            //TODO can add different handlers as needed in the future
            default:
                throw new IllegalStateException("Unexpected value: " + messageType);

        }
    }
}
