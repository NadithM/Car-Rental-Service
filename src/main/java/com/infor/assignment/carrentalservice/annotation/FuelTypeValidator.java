package com.infor.assignment.carrentalservice.annotation;

import com.infor.assignment.carrentalservice.model.vehicle.Fuel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FuelTypeValidator implements ConstraintValidator<FuelType, String> {

    private List<String> validSet;

    @Override
    public void initialize(FuelType constraint) {
        this.validSet = Arrays.stream(Fuel.values()).map(a -> a.name()).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean status = value == null || validSet.contains(value);
        if (!status) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid fuelType : " + value + ". Valid fuelTypes are " + Arrays.stream(Fuel.values()).map(a -> a.name()).collect(Collectors.toList())).addConstraintViolation();
            return false;
        }
        return true;
    }


}