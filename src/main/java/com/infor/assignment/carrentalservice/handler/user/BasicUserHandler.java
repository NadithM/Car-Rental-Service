package com.infor.assignment.carrentalservice.handler.user;


import com.infor.assignment.carrentalservice.exception.UserNotFound;
import com.infor.assignment.carrentalservice.exception.VehicleNotFound;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.model.user.User;
import org.springframework.stereotype.Component;

@Component
public abstract class BasicUserHandler<T extends BasicRequestCriteria, O extends User> {

    public abstract O registerUser(T vehicleRegisterRequestCriteria);

    public abstract O retrieveUser(T vehicleRegisterRequestCriteria) throws VehicleNotFound, UserNotFound;


}
