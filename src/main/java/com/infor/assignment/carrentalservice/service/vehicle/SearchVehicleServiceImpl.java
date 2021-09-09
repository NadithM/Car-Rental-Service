package com.infor.assignment.carrentalservice.service.vehicle;

import com.infor.assignment.carrentalservice.exception.ServiceException;
import com.infor.assignment.carrentalservice.handler.vehicle.BasicVehicleHandler;
import com.infor.assignment.carrentalservice.model.vehicle.Vehicle;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.service.AbstractExecutorService;
import com.infor.assignment.carrentalservice.service.AbstractVehicleServiceHandler;
import com.infor.assignment.carrentalservice.util.MessageHeaders;
import com.infor.assignment.carrentalservice.util.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SearchVehicleServiceImpl extends AbstractVehicleServiceHandler implements AbstractExecutorService {

    @ServiceActivator(inputChannel = "defaultCommonVehicleSearchChannel")
    public Message<Void> executeProcess(Message<? extends BasicRequestCriteria> vehicleCriteriaMessage) {

        BasicRequestCriteria basicRequestCriteria = vehicleCriteriaMessage.getPayload();
        String requestId = vehicleCriteriaMessage.getHeaders().get(MessageHeaders.REQUEST_ID, String.class);
        MessageType messageType = vehicleCriteriaMessage.getHeaders().get(MessageHeaders.MESSAGE_TYPE, MessageType.class);

        try {
            // BasicVehicleHandler will take care of any Vehicle Type introduced in the future. for now it's only CAR
            BasicVehicleHandler basicVehicleHandler = getHandler(messageType);
            List<Vehicle> vehicle = basicVehicleHandler.search(basicRequestCriteria);
            log.info("messageType: {},requestId: {},request completed with success", messageType, requestId);
            basicRequestCriteria.getDeferredResult().setResult(vehicle);
        } catch (Exception e) {
            log.info("messageType: {},requestId: {},request is failed due to unexpected error : {}", messageType, requestId, e.getMessage());
            ServiceException serviceException = new ServiceException(e.getMessage());
            basicRequestCriteria.getDeferredResult().setErrorResult(serviceException);
        }
        return null;
    }
}
