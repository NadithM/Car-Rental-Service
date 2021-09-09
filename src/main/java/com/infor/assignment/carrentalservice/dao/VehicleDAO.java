package com.infor.assignment.carrentalservice.dao;

import com.infor.assignment.carrentalservice.exception.VehicleNotFound;
import com.infor.assignment.carrentalservice.mockdb.VehicleRepository;
import com.infor.assignment.carrentalservice.model.vehicle.Vehicle;
import com.infor.assignment.carrentalservice.util.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//It's better we can use DAO layers to call repository

@Component
public class VehicleDAO {

    @Autowired
    VehicleRepository vehicleRepository;

    //TODO:: in real case, all of this methods can be Query to database tables
    public Vehicle getVehicleInfoById(String id) {
        return vehicleRepository.getVehicleInfoById(id);
    }

    public Vehicle getVehicleInfoByPlateId(String platId) {
        return vehicleRepository.getVehicleInfoByPlateId(platId);
    }

    public List<Vehicle> getAllActiveVehicles() {
        return vehicleRepository.getAllActiveVehicles();
    }


    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle modify(Vehicle vehicleInfo) throws VehicleNotFound {
        return vehicleRepository.modify(vehicleInfo);
    }
}
