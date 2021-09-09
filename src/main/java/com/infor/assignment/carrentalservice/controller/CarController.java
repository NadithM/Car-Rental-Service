package com.infor.assignment.carrentalservice.controller;

import com.infor.assignment.carrentalservice.handler.RestRequestHandler;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.model.vehicle.CarAvailabilityRequest;
import com.infor.assignment.carrentalservice.model.vehicle.CarKeyControls;
import com.infor.assignment.carrentalservice.model.vehicle.CarRegisterRequest;
import com.infor.assignment.carrentalservice.model.vehicle.CarRequestCriteria;
import com.infor.assignment.carrentalservice.util.MessageType;
import com.infor.assignment.carrentalservice.util.VehicleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.UUID;


// it's better if we can implement this controller using an interface first and move all meta-data there( such as RequestMapping,API documentation if there's any like swagger)
@RestController
@Slf4j
@RequestMapping("/v1/vehicles/cars")
@Validated
public class CarController {

    @Autowired
    private RestRequestHandler restRequestHandler;

    @GetMapping
    public DeferredResult<ResponseEntity<?>> getCar(@RequestParam(required = false) String plateId, @RequestParam(required = false) String id) throws Exception {
        String requestId = UUID.randomUUID().toString();
        CarRequestCriteria carRegisterRequestCriteria = new CarRequestCriteria();
        carRegisterRequestCriteria.setVehicleType(VehicleType.CAR);
        carRegisterRequestCriteria.setRequestId(requestId);
        carRegisterRequestCriteria.setPlateId(plateId);
        carRegisterRequestCriteria.setId(id);
        Message<BasicRequestCriteria> lookupRequestCriteriaMessage = restRequestHandler.handleRequest(MessageType.GET_CAR, carRegisterRequestCriteria);
        return lookupRequestCriteriaMessage.getPayload().getDeferredResult();
    }

    @PostMapping
    public DeferredResult<ResponseEntity<?>> registerCar(@Valid @RequestBody CarRegisterRequest carRegisterRequest) throws Exception {
        String requestId = UUID.randomUUID().toString();
        CarRequestCriteria carRegisterRequestCriteria = new CarRequestCriteria();
        carRegisterRequestCriteria.setVehicleType(VehicleType.CAR);
        carRegisterRequestCriteria.setRequestId(requestId);
        carRegisterRequestCriteria.setCarRegisterRequest(carRegisterRequest);
        Message<BasicRequestCriteria> lookupRequestCriteriaMessage = restRequestHandler.handleRequest(MessageType.REGISTER_CAR, carRegisterRequestCriteria);
        return lookupRequestCriteriaMessage.getPayload().getDeferredResult();
    }

    @PostMapping("/availability")
    public DeferredResult<ResponseEntity<?>> registerAvailabilityCar(@Valid @RequestBody CarAvailabilityRequest carAvailabilityRequest) throws Exception {
        String requestId = UUID.randomUUID().toString();
        CarRequestCriteria carRegisterRequestCriteria = new CarRequestCriteria();
        carRegisterRequestCriteria.setVehicleType(VehicleType.CAR);
        carRegisterRequestCriteria.setRequestId(requestId);
        carRegisterRequestCriteria.setPlateId(carAvailabilityRequest.getPlateId());
        carRegisterRequestCriteria.setCarAvailabilityRequest(carAvailabilityRequest);
        Message<BasicRequestCriteria> lookupRequestCriteriaMessage = restRequestHandler.handleRequest(MessageType.REGISTER_AVAILABILITY_CAR, carRegisterRequestCriteria);
        return lookupRequestCriteriaMessage.getPayload().getDeferredResult();
    }


    @PostMapping("/search")
    public DeferredResult<ResponseEntity<?>> searchCar(@Valid @RequestBody CarKeyControls carKeyControls) throws Exception {
        String requestId = UUID.randomUUID().toString();
        CarRequestCriteria carRegisterRequestCriteria = new CarRequestCriteria();
        carRegisterRequestCriteria.setVehicleType(VehicleType.CAR);
        carRegisterRequestCriteria.setRequestId(requestId);
        carRegisterRequestCriteria.setKeyControls(carKeyControls);
        Message<BasicRequestCriteria> lookupRequestCriteriaMessage = restRequestHandler.handleRequest(MessageType.SEARCH_AVAILABLE_CARS, carRegisterRequestCriteria);
        return lookupRequestCriteriaMessage.getPayload().getDeferredResult();
    }

}
