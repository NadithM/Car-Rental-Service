package com.infor.assignment.carrentalservice.service.order;

import com.infor.assignment.carrentalservice.exception.NoAvailabilityException;
import com.infor.assignment.carrentalservice.exception.OrderNotFound;
import com.infor.assignment.carrentalservice.exception.ServiceException;
import com.infor.assignment.carrentalservice.handler.order.BasicOrderHandler;
import com.infor.assignment.carrentalservice.model.common.ErrorResponse;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.model.order.Order;
import com.infor.assignment.carrentalservice.service.AbstractExecutorService;
import com.infor.assignment.carrentalservice.service.AbstractOrderServiceHandler;
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
public class OrderServiceImpl extends AbstractOrderServiceHandler implements AbstractExecutorService {

    @Override
    @ServiceActivator(inputChannel = "orderManagementChannel")
    public Message<Void> executeProcess(Message<? extends BasicRequestCriteria> orderCriteria) {
        BasicRequestCriteria basicRequestCriteria = orderCriteria.getPayload();
        String requestId = orderCriteria.getHeaders().get(MessageHeaders.REQUEST_ID, String.class);
        MessageType messageType = orderCriteria.getHeaders().get(MessageHeaders.MESSAGE_TYPE, MessageType.class);

        try {
            Order order = null;
            BasicOrderHandler basicOrderHandler = getHandler(messageType);
            if (MessageType.CREATE_ORDER.equals(messageType)) {
                order = basicOrderHandler.createOrder(basicRequestCriteria);
            } else if (MessageType.GET_ORDER.equals(messageType)) {
                order = basicOrderHandler.getOrder(basicRequestCriteria);
            }
            log.info("messageType: {}, requestId: {},request completed with success", messageType, requestId);
            basicRequestCriteria.getDeferredResult().setResult(order);
        } catch (OrderNotFound e) {
            log.info("messageType: {},requestId: {},request is failed due to not found: {}", messageType, requestId, e.getMessage());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setErrorInfo(e.getMessage())
                    .build();
            basicRequestCriteria.getDeferredResult().setResult(ResponseEntity.status(errorResponse.getStatus()).body(errorResponse.getErrorInfo()));
        } catch (NoAvailabilityException e) {
            log.info("messageType: {},requestId: {},request is failed due to duplicate record : {}", messageType, requestId, e.getMessage());
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
