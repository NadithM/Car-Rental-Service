package com.infor.assignment.carrentalservice.handler.vehicle;


import com.infor.assignment.carrentalservice.dao.VehicleDAO;
import com.infor.assignment.carrentalservice.exception.VehicleNotFound;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.model.common.DateRange;
import com.infor.assignment.carrentalservice.model.vehicle.KeyControls;
import com.infor.assignment.carrentalservice.model.vehicle.Vehicle;
import com.infor.assignment.carrentalservice.util.DateUtil;
import com.infor.assignment.carrentalservice.util.FilterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract class BasicVehicleHandler<T extends BasicRequestCriteria, O extends Vehicle, K extends KeyControls> {

    // These are the parameters of filtering criteria for Basic Vehicle Search
    public static int[] filteringCriteria = new int[]{
            FilterParam.PARAM_VEHICLE_TYPE,
            FilterParam.PARAM_BRAND,
            FilterParam.PARAM_TYPE_OF_FUEL,
            FilterParam.PARAM_TRANSMISSION,
            FilterParam.PARAM_FEATURES,
            FilterParam.PARAM_MINIMUM_RATE_PER_HOUR,
            FilterParam.PARAM_MAXIMUM_RATE_PER_HOUR
    };

    @Autowired
    private VehicleDAO vehicleDAO;

    @Autowired
    DateUtil dateUtil;

    public abstract O registerVehicle(T vehicleRegisterRequestCriteria);

    public abstract O registerVehicleAvailability(T vehicleRegisterRequestCriteria) throws VehicleNotFound;

    public abstract O retrieveVehicle(T vehicleRegisterRequestCriteria) throws VehicleNotFound;


    public List<? extends Vehicle> search(T vehicleRegisterRequestCriteria) {
        // supportsa any K extends KeyControls
        K filteringKeyControls = (K) vehicleRegisterRequestCriteria.getKeyControls();

        //first get all active vehicles
        List<Vehicle> allActiveVehicles = vehicleDAO.getAllActiveVehicles();

        //filter based on available dates of the vehicle
        List<O> activeCarsByDateTime = allActiveVehicles.stream().filter(vehicle -> {
                    List<DateRange> requestedDateRanges = filteringKeyControls.getAvailableDateRanges();
                    for (DateRange requestedDateRange : requestedDateRanges) {
                        boolean withinRange = false;
                        for (DateRange actualAvailability : vehicle.getAvailableDates()) {
                            withinRange = dateUtil.isWithinRange(actualAvailability, requestedDateRange);
                            if (withinRange) return true;
                        }
                    }
                    return false;
                })
                .map(a -> (O) a)
                .collect(Collectors.toList());

        //filter based on black out of the vehicle's existing orders
        List<O> nextList = activeCarsByDateTime.stream().filter(vehicle -> {
                    List<DateRange> requestedDateRanges = filteringKeyControls.getAvailableDateRanges();
                    for (DateRange requestedDateRange : requestedDateRanges) {
                        boolean isOverlapping = false;
                        if (vehicle.getBlackOutDatesByOrder() != null) {
                            for (DateRange blackOutDateRange : vehicle.getBlackOutDatesByOrder().values()) {
                                isOverlapping = dateUtil.isOverlapping(blackOutDateRange, requestedDateRange);
                                if (isOverlapping) return false;
                            }
                        }
                    }
                    return true;
                })
                .map(a -> (O) a)
                .collect(Collectors.toList());


        List<O> tempList = null;
        //filter based on optional parameters
        for (int param : filteringCriteria) {
            Object paramValue = getValue(filteringKeyControls, param);
            if (paramValue == null || (paramValue instanceof Number && ((Number) paramValue).intValue() <= 0)) {
                continue;
            }
            if (paramValue instanceof List) {
                tempList = filterVehiclesOnMultipleValues(nextList, (List<String>) paramValue, param);
            } else {
                tempList = filterVehiclesOnSingleValues(nextList, (String) paramValue, param);
            }

            nextList = tempList;
        }

        return nextList;
    }

    //filter based on when there is multiple values in the search criteria for the given field
    private List<O> filterVehiclesOnMultipleValues(List<O> nextList, List<String> requestedValues, int param) {
        if (requestedValues.isEmpty()) return nextList;
        switch (param) {
            case FilterParam.PARAM_VEHICLE_TYPE:
                // default to OR Operation since it's a singleValued for a given vehicle
                return nextList.stream().filter(vehicle -> requestedValues.contains(vehicle.getType().name())).collect(Collectors.toList());
            case FilterParam.PARAM_BRAND:
                // default to OR Operation since it's a singleValued for a given vehicle
                return nextList.stream().filter(vehicle -> requestedValues.contains(vehicle.getBrand().name())).collect(Collectors.toList());
            case FilterParam.PARAM_TYPE_OF_FUEL:
                // default to OR Operation since it's a singleValued for a given vehicle
                return nextList.stream().filter(vehicle -> requestedValues.contains(vehicle.getTypeOfFuel().name())).collect(Collectors.toList());
            case FilterParam.PARAM_TRANSMISSION:
                // default to OR Operation since assuming it's a singleValued for a given vehicle
                return nextList.stream().filter(vehicle -> requestedValues.contains(vehicle.getTransmission().name())).collect(Collectors.toList());
            case FilterParam.PARAM_FEATURES:
                // default to AND Operation since it's a multiValued for a given vehicle
                return nextList.stream().filter(vehicle -> vehicle.getFeatures().stream().map(feature -> feature.name()).collect(Collectors.toList()).containsAll(requestedValues)).collect(Collectors.toList());
            default:
                // filtering based on Vehicle type specific parameters
                return customFilterVehiclesOnMultipleValues(nextList, requestedValues, param);
        }
    }

    private List<O> filterVehiclesOnSingleValues(List<O> nextList, String requestedValue, int param) {
        if (requestedValue.length() == 0) return nextList;
        switch (param) {
            case FilterParam.PARAM_MINIMUM_RATE_PER_HOUR:
                // default to greater than or equal to Operation since we are talking about minima
                return nextList.stream().filter(vehicle -> vehicle.getRentalPricePerHour() >= Double.valueOf(requestedValue)).collect(Collectors.toList());
            case FilterParam.PARAM_MAXIMUM_RATE_PER_HOUR:
                // default to less than or equal to Operation since we are talking about maxima
                return nextList.stream().filter(vehicle -> vehicle.getRentalPricePerHour() <= Double.valueOf(requestedValue)).collect(Collectors.toList());
            default:
                return customFilterVehiclesOnSingleValues(nextList, requestedValue, param);
        }
    }

    // retrieve the paramValue from request
    protected Object getValue(K keyControls, int param) {
        switch (param) {
            case FilterParam.PARAM_VEHICLE_TYPE:
                return keyControls.getType().stream().map(a -> a.name()).collect(Collectors.toList());
            case FilterParam.PARAM_BRAND:
                return keyControls.getBrand().stream().collect(Collectors.toList());
            case FilterParam.PARAM_TYPE_OF_FUEL:
                return keyControls.getTypeOfFuel().stream().map(a -> a.name()).collect(Collectors.toList());
            case FilterParam.PARAM_TRANSMISSION:
                return keyControls.getTransmission().stream().map(a -> a.name()).collect(Collectors.toList());
            case FilterParam.PARAM_FEATURES:
                return keyControls.getFeatures().stream().map(a -> a.name()).collect(Collectors.toList());
            case FilterParam.PARAM_MINIMUM_RATE_PER_HOUR:
                return String.valueOf(keyControls.getMinimumRate());
            case FilterParam.PARAM_MAXIMUM_RATE_PER_HOUR:
                return String.valueOf(keyControls.getMaximumRate());
            default:
                return customVehicleGetValue(keyControls, param);
        }
    }

    // to extend the filtering feature to new parameters
    public abstract Object customVehicleGetValue(K keyControls, int param);

    // to extend the filtering feature to new parameters
    public abstract List<O> customFilterVehiclesOnSingleValues(List<O> nextList, String requestedValue, int param);

    // to extend the filtering feature to new parameters
    public abstract List<O> customFilterVehiclesOnMultipleValues(List<O> nextList, List<String> requestedValue, int param);
}
