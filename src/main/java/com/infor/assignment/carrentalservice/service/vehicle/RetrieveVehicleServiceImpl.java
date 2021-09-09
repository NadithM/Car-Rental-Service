package com.infor.assignment.carrentalservice.service.vehicle;

import com.infor.assignment.carrentalservice.exception.ServiceException;
import com.infor.assignment.carrentalservice.exception.VehicleNotFound;
import com.infor.assignment.carrentalservice.model.common.ErrorResponse;
import com.infor.assignment.carrentalservice.handler.vehicle.BasicVehicleHandler;
import com.infor.assignment.carrentalservice.model.vehicle.Vehicle;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.service.AbstractExecutorService;
import com.infor.assignment.carrentalservice.service.AbstractVehicleServiceHandler;
import com.infor.assignment.carrentalservice.util.MessageHeaders;
import com.infor.assignment.carrentalservice.util.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RetrieveVehicleServiceImpl extends AbstractVehicleServiceHandler implements AbstractExecutorService {

    @Override
    @ServiceActivator(inputChannel = "defaultCommonVehicleInfoRetrieveChannel")
    public Message<Void> executeProcess(Message<? extends BasicRequestCriteria> vehicleCriteriaMessage) {

        BasicRequestCriteria basicRequestCriteria = vehicleCriteriaMessage.getPayload();
        String requestId = vehicleCriteriaMessage.getHeaders().get(MessageHeaders.REQUEST_ID, String.class);
        MessageType messageType = vehicleCriteriaMessage.getHeaders().get(MessageHeaders.MESSAGE_TYPE, MessageType.class);

        try {
            // BasicVehicleHandler will take care of any Vehicle Type introduced in the future. for now it's only CAR
            BasicVehicleHandler basicVehicleHandler = getHandler(messageType);
            Vehicle vehicle = basicVehicleHandler.retrieveVehicle(basicRequestCriteria);
            log.info("messageType: {},requestId: {},request completed with success", messageType, requestId);
            basicRequestCriteria.getDeferredResult().setResult(vehicle);
        } catch (VehicleNotFound e) {
            log.info("messageType: {},requestId: {},request is failed due to not found: {}", messageType, requestId, e.getMessage());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setErrorInfo(e.getMessage())
                    .build();
            basicRequestCriteria.getDeferredResult().setResult(ResponseEntity.status(errorResponse.getStatus()).body(errorResponse.getErrorInfo()));
        } catch (Exception e) {
            log.info("messageType: {},requestId: {},request is failed due to unexpected error : {}", messageType, requestId, e.getMessage());
            ServiceException serviceException = new ServiceException(e.getMessage());
            basicRequestCriteria.getDeferredResult().setErrorResult(serviceException);
        }
        return null;
    }
}
