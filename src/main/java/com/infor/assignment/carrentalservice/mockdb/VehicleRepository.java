package com.infor.assignment.carrentalservice.mockdb;

import com.infor.assignment.carrentalservice.exception.DuplicateVehicleFoundException;
import com.infor.assignment.carrentalservice.exception.VehicleNotFound;
import com.infor.assignment.carrentalservice.model.vehicle.Vehicle;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

//DUMMY DB LAYER
//TODO:: in real case, all of these methods replaced with ORM,JPA Framework or using Spring-Data
@Repository
public class VehicleRepository {

    private Map<String, Vehicle> vehicleInfoRepo = new ConcurrentHashMap<>();
    //just using unique autoIncrementId to mock auto increment id done by ORM Framework.
    private AtomicInteger index = new AtomicInteger(1000);


    public Vehicle getVehicleInfoById(String id) {
        Vehicle vehicle = vehicleInfoRepo.get(id);
        return vehicle;
    }

    public List<Vehicle> getAll() {
        List<Vehicle> vehicles = vehicleInfoRepo.values().stream().collect(Collectors.toList());
        return vehicles;
    }

    public Vehicle getVehicleInfoByPlateId(String platId) {
        Optional<Vehicle> foundVehicle = vehicleInfoRepo.entrySet().stream().filter(entry -> entry.getValue().getPlateId().equals(platId)).map(entry -> entry.getValue()).findFirst();
        if (foundVehicle.isPresent()) return foundVehicle.get();
        return null;
    }

    public List<Vehicle> getAllVehiclesInfoByType(String type) {
        List<Vehicle> allVehicles = vehicleInfoRepo.entrySet().stream().filter(entry -> entry.getValue().getType().name().equals(type)).map(entry -> entry.getValue()).collect(Collectors.toList());
        return allVehicles;
    }

    public List<Vehicle> getAllActiveVehicles() {
        List<Vehicle> allVehicles = vehicleInfoRepo.values().stream().filter(vehicle -> vehicle.isActive()).collect(Collectors.toList());
        return allVehicles;
    }

    public Vehicle save(Vehicle vehicle) {
        Optional<Vehicle> foundVehicle = vehicleInfoRepo.entrySet().stream().filter(entry -> entry.getValue().getPlateId().equals(vehicle.getPlateId())).map(entry -> entry.getValue()).findFirst();
        if (!foundVehicle.isPresent()) {
            vehicle.setId(System.currentTimeMillis() + String.format("%05d", index.incrementAndGet()));
            vehicleInfoRepo.put(vehicle.getId(), vehicle);
            return vehicle;
        }
        throw new DuplicateVehicleFoundException("Duplicate Record Found");
    }

    public Vehicle modify(Vehicle vehicle) throws VehicleNotFound {
        Optional<Vehicle> foundVehicle = vehicleInfoRepo.entrySet().stream().filter(entry -> entry.getValue().getId().equals(vehicle.getId())).map(entry -> entry.getValue()).findFirst();
        if (foundVehicle.isPresent()) {
            vehicleInfoRepo.put(vehicle.getId(), vehicle);
            return vehicle;
        }
        throw new VehicleNotFound("No Car Found", vehicle.getId(), vehicle.getPlateId());
    }


}
