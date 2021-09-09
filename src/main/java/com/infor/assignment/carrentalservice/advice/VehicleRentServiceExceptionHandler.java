package com.infor.assignment.carrentalservice.advice;

import com.infor.assignment.carrentalservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class VehicleRentServiceExceptionHandler extends ResponseEntityExceptionHandler {


    // This method will handle invalid parameters in the request and will give a detail error response
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMap.put(Constants.LOCATION, fieldName);
            errorMap.put(Constants.ERROR_INFO, message);
            errorMap.put(Constants.STATUS, String.valueOf(HttpStatus.BAD_REQUEST.value()));
        });
        return handleException(errorMap, HttpStatus.BAD_REQUEST);
    }

    // This method will handle unexpected errors and will give a structured error response
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(Constants.ERROR_INFO, ex.getMessage());
        errorMap.put(Constants.STATUS, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return handleException(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> handleException(Map<String, String> errorMap, HttpStatus httpStatus) {
        errorMap.put(Constants.STATUS, String.valueOf(httpStatus.value()));
        return ResponseEntity.status(httpStatus).body(errorMap);
    }

}
