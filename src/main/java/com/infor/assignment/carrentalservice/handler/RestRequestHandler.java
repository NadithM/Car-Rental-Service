package com.infor.assignment.carrentalservice.handler;

import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.model.common.ErrorResponse;
import com.infor.assignment.carrentalservice.util.MessageHeaders;
import com.infor.assignment.carrentalservice.util.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
@Slf4j
public class RestRequestHandler {

    @Autowired
    private CommonMessageHandlingGateway commonMessageHandlingGateway;

    @Value("${rest.request.timeout.general:2000}")
    private long timeoutForRestCall;

    @Value("${rest.request.timeout.vehicle-search:5000}")
    private long timeoutForRestVehicleSearch;

    @Value("${rest.request.timeout.order-vehicle:5000}")
    private long timeoutForRestOrderVehicle;

    public Message<BasicRequestCriteria> handleRequest(MessageType messageType, BasicRequestCriteria basicRequestCriteria) {

        String requestId = basicRequestCriteria.getRequestId();
        DeferredResult<ResponseEntity<?>> response = new DeferredResult<>(setTimeoutBasedOnMessageType(messageType));
        basicRequestCriteria.setMessageType(messageType);
        basicRequestCriteria.setDeferredResult(response);
        response.onCompletion(() -> {
            log.info("messageType: {}, Request Successfully Completed,Request Id {} ", messageType, requestId);
        });
        response.onError(throwable -> {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setErrorInfo(throwable.getMessage())
                    .build();
            response.setErrorResult(ResponseEntity.status(errorResponse.getStatus()).body(errorResponse.getErrorInfo()));
            log.info("messageType: {}, Error in processing response,Request Id {} ", messageType, requestId);
        });
        response.onTimeout(() -> {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .setStatus(HttpStatus.REQUEST_TIMEOUT)
                    .setErrorInfo("request Timeout")
                    .build();
            //default timeout response
            response.setErrorResult(ResponseEntity.status(errorResponse.getStatus()).body(errorResponse.getErrorInfo()));
            log.info("messageType: {}, Request timeout occurred,Request Id {} ", messageType, requestId);
        });

        Message<BasicRequestCriteria> criteriaMessage = MessageBuilder
                .withPayload(basicRequestCriteria)
                .setHeader(MessageHeaders.REQUEST_ID, basicRequestCriteria.getRequestId())
                .setHeader(MessageHeaders.MESSAGE_TYPE, basicRequestCriteria.getMessageType())
                .build();
        commonMessageHandlingGateway.send(criteriaMessage);
        return criteriaMessage;
    }

    private long setTimeoutBasedOnMessageType(MessageType messageType) {
        switch (messageType) {
            case CREATE_ORDER:
                return timeoutForRestOrderVehicle;
            case SEARCH_AVAILABLE_CARS:
                return timeoutForRestVehicleSearch;
            //TODO we can add timeout depend on the requirement
            default:
                return timeoutForRestCall;

        }
    }
}
