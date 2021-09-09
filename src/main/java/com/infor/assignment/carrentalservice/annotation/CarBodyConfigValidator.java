package com.infor.assignment.carrentalservice.annotation;

import com.infor.assignment.carrentalservice.model.vehicle.CarBodyConfiguration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CarBodyConfigValidator implements ConstraintValidator<CarBodyConfigType, String> {

    private List<String> validSet;

    @Override
    public void initialize(CarBodyConfigType constraint) {
        this.validSet = Arrays.stream(CarBodyConfiguration.values()).map(a -> a.name()).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean status = value == null || validSet.contains(value);
        if (!status) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid carBodyConfiguration : " + value + ". Valid car body configurations are " + Arrays.stream(CarBodyConfiguration.values()).map(a -> a.name()).collect(Collectors.toList())).addConstraintViolation();
            return false;
        }
        return true;
    }


}