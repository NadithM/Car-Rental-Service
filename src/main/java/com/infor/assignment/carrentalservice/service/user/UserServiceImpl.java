package com.infor.assignment.carrentalservice.service.user;

import com.infor.assignment.carrentalservice.exception.*;
import com.infor.assignment.carrentalservice.handler.user.BasicUserHandler;
import com.infor.assignment.carrentalservice.model.common.ErrorResponse;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.model.user.User;
import com.infor.assignment.carrentalservice.service.AbstractExecutorService;
import com.infor.assignment.carrentalservice.service.AbstractUserServiceHandler;
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
public class UserServiceImpl extends AbstractUserServiceHandler implements AbstractExecutorService {

    @Override
    @ServiceActivator(inputChannel = "userManageChannel")
    public Message<Void> executeProcess(Message<? extends BasicRequestCriteria> vehicleCriteriaMessage) {
        BasicRequestCriteria basicRequestCriteria = vehicleCriteriaMessage.getPayload();
        String requestId = vehicleCriteriaMessage.getHeaders().get(MessageHeaders.REQUEST_ID, String.class);
        MessageType messageType = vehicleCriteriaMessage.getHeaders().get(MessageHeaders.MESSAGE_TYPE, MessageType.class);

        try {
            User user = null;
            BasicUserHandler basicUserHandler = getHandler(messageType);
            if (MessageType.REGISTER_USER.equals(messageType)) {
                user = basicUserHandler.registerUser(basicRequestCriteria);
            } else if (MessageType.GET_USER.equals(messageType)) {
                user = basicUserHandler.retrieveUser(basicRequestCriteria);
            }
            log.info("messageType: {}, requestId: {},request completed with success", messageType, requestId);
            basicRequestCriteria.getDeferredResult().setResult(user);
        } catch (UserNotFound e) {
            log.info("messageType: {},requestId: {},request is failed due to not found: {}", messageType, requestId, e.getMessage());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setErrorInfo(e.getMessage())
                    .build();
            basicRequestCriteria.getDeferredResult().setResult(ResponseEntity.status(errorResponse.getStatus()).body(errorResponse.getErrorInfo()));
        } catch (DuplicateUserFoundException e) {
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
