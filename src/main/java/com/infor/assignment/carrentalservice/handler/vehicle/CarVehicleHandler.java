package com.infor.assignment.carrentalservice.handler.vehicle;


import com.infor.assignment.carrentalservice.annotation.Brand;
import com.infor.assignment.carrentalservice.dao.VehicleDAO;
import com.infor.assignment.carrentalservice.exception.VehicleNotFound;
import com.infor.assignment.carrentalservice.model.vehicle.*;
import com.infor.assignment.carrentalservice.util.DateUtil;
import com.infor.assignment.carrentalservice.util.FilterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarVehicleHandler extends BasicVehicleHandler<CarRequestCriteria, Car, CarKeyControls> {


    @Autowired
    private VehicleDAO vehicleDAO;

    @Autowired
    DateUtil dateUtil;

    @PostConstruct
    public void init() {
        filteringCriteria = new int[]{
                FilterParam.PARAM_VEHICLE_TYPE,
                FilterParam.PARAM_BRAND,
                FilterParam.PARAM_TYPE_OF_FUEL,
                FilterParam.PARAM_TRANSMISSION,
                FilterParam.PARAM_FEATURES,
                FilterParam.PARAM_MINIMUM_RATE_PER_HOUR,
                FilterParam.PARAM_MAXIMUM_RATE_PER_HOUR,
                FilterParam.PARAM_CAR_BODY_CONFIGURATION,
        };
    }

    @Override
    public synchronized Car registerVehicle(CarRequestCriteria vehicleRegisterRequestCriteria) {
        CarRegisterRequest carRegisterRequest = vehicleRegisterRequestCriteria.getCarRegisterRequest();
        Car car = new Car();

        car.setPlateId(carRegisterRequest.getPlateNumber());
        if (carRegisterRequest.getBrand() != null) {
            car.setBrand(Brand.valueOf(carRegisterRequest.getBrand()));
        }
        if (carRegisterRequest.getCarBodyConfiguration() != null) {
            car.setCarBodyConfiguration(CarBodyConfiguration.valueOf(carRegisterRequest.getCarBodyConfiguration()));
        }
        if (carRegisterRequest.getTransmission() != null) {
            car.setTransmission(Transmission.valueOf(carRegisterRequest.getTransmission()));
        }
        if (carRegisterRequest.getFeatures() != null) {
            List<Feature> features = carRegisterRequest.getFeatures().stream().map(a -> Feature.valueOf(a)).collect(Collectors.toList());
            car.setFeatures(features);
        }
        if (carRegisterRequest.getTypeOfFuel() != null) {
            car.setTypeOfFuel(Fuel.valueOf(carRegisterRequest.getTypeOfFuel()));
        }
        return (Car) vehicleDAO.save(car);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    // @Transactional : This is important to manage transactions atomicity. if the database is there,We can Enable Transaction Management and use this Isolation levels

    public synchronized Car registerVehicleAvailability(CarRequestCriteria carRequestCriteria) throws VehicleNotFound {
        Vehicle vehicleInfo = null;
        boolean active = false;
        if (carRequestCriteria.getPlateId() != null) {
            // when we get the value from vehicle table, records will be on read/write lock until commits again.
            vehicleInfo = vehicleDAO.getVehicleInfoByPlateId(carRequestCriteria.getPlateId());
        }
        if (vehicleInfo == null) {
            throw new VehicleNotFound("Car is not found by plate Id: " + carRequestCriteria.getPlateId(), carRequestCriteria.getId(), carRequestCriteria.getPlateId());
        }

        //if null keep do not modify
        //if empty will modify. means no dates
        if (carRequestCriteria.getCarAvailabilityRequest().getAvailableDates() != null) {
            vehicleInfo.setAvailableDates(carRequestCriteria.getCarAvailabilityRequest().getAvailableDates());
            active = !active ? !carRequestCriteria.getCarAvailabilityRequest().getAvailableDates().isEmpty() : active;
        }
        if (carRequestCriteria.getCarAvailabilityRequest().getRentalPricePerHour() != null) {
            vehicleInfo.setRentalPricePerHour(carRequestCriteria.getCarAvailabilityRequest().getRentalPricePerHour());
        }
        vehicleInfo.setActive(active);
        return (Car) vehicleDAO.modify(vehicleInfo);
        //Lock on vehicle record will be releasing
    }

    @Override
    public Car retrieveVehicle(CarRequestCriteria vehicleRegisterRequestCriteria) throws VehicleNotFound {
        Vehicle vehicleInfo = null;
        if (vehicleRegisterRequestCriteria.getId() != null) {
            vehicleInfo = vehicleDAO.getVehicleInfoById(vehicleRegisterRequestCriteria.getId());
        } else if (vehicleRegisterRequestCriteria.getPlateId() != null) {
            vehicleInfo = vehicleDAO.getVehicleInfoByPlateId(vehicleRegisterRequestCriteria.getPlateId());
        }
        if (vehicleInfo == null) {
            throw new VehicleNotFound("Requested Car is not found", vehicleRegisterRequestCriteria.getId(), vehicleRegisterRequestCriteria.getPlateId());
        }
        return (Car) vehicleInfo;
    }

    public List<Car> customFilterVehiclesOnMultipleValues(List<Car> nextList, List<String> requestedValues, int param) {
        switch (param) {
            case FilterParam.PARAM_CAR_BODY_CONFIGURATION:
                return nextList.stream().filter(vehicle -> requestedValues.contains(vehicle.getCarBodyConfiguration().name())).collect(Collectors.toList());
        }

        return nextList;
    }

    @Override
    public Object customVehicleGetValue(CarKeyControls carKeyControls, int param) {
        switch (param) {
            case FilterParam.PARAM_CAR_BODY_CONFIGURATION:
                return carKeyControls.getCarBodyConfigurations().stream().map(a -> a.name()).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Car> customFilterVehiclesOnSingleValues(List<Car> nextList, String requestedValue, int param) {
        switch (param) {
            case FilterParam.PARAM_MINIMUM_RATE_PER_HOUR:
                return nextList.stream().filter(vehicle -> vehicle.getRentalPricePerHour() >= Double.valueOf(requestedValue)).collect(Collectors.toList());
            case FilterParam.PARAM_MAXIMUM_RATE_PER_HOUR:
                return nextList.stream().filter(vehicle -> vehicle.getRentalPricePerHour() <= Double.valueOf(requestedValue)).collect(Collectors.toList());
        }
        return nextList;
    }

}
