package com.infor.assignment.carrentalservice.annotation;

import com.infor.assignment.carrentalservice.model.vehicle.Transmission;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransmissionTypeValidator implements ConstraintValidator<TransmissionType, String> {

    private List<String> validSet;

    @Override
    public void initialize(TransmissionType constraint) {
        this.validSet = Arrays.stream(Transmission.values()).map(a -> a.name()).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean status = value == null || validSet.contains(value);
        if (!status) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid transmission : " + value + ". Valid transmission types are " + Arrays.stream(Transmission.values()).map(a -> a.name()).collect(Collectors.toList())).addConstraintViolation();
            return false;
        }
        return true;
    }

}