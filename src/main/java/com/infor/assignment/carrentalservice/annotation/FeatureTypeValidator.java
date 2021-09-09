package com.infor.assignment.carrentalservice.annotation;

import com.infor.assignment.carrentalservice.model.vehicle.Feature;
import com.infor.assignment.carrentalservice.model.vehicle.Fuel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureTypeValidator implements ConstraintValidator<FeatureType, List> {

    private List<String> validSet;

    @Override
    public void initialize(FeatureType constraint) {

        this.validSet = Arrays.stream(Feature.values()).map(a -> a.name()).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(List value, ConstraintValidatorContext context) {
        boolean status = value == null || validSet.containsAll(value);
        if (!status) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid Features : " + value + ". Valid features types are " + Arrays.stream(Feature.values()).map(a -> a.name()).collect(Collectors.toList())).addConstraintViolation();
            return false;
        }
        return true;
    }

}