package com.infor.assignment.carrentalservice.service;

import com.infor.assignment.carrentalservice.handler.user.BasicUserHandler;
import com.infor.assignment.carrentalservice.handler.user.UserHandler;
import com.infor.assignment.carrentalservice.util.MessageType;
import org.springframework.beans.factory.annotation.Autowired;

// This is to Handle USer Operations. when new Operation or API introduced to User flow we can add message-type here in the switch case
public abstract class AbstractUserServiceHandler {

    @Autowired
    private UserHandler userHandler;

    protected BasicUserHandler getHandler(MessageType messageType) {
        switch (messageType) {
            case REGISTER_USER:
            case GET_USER:
                return userHandler;

            //TODO can add different handlers as needed in the future
            default:
                throw new IllegalStateException("Unexpected value: " + messageType);

        }
    }
}
