package com.infor.assignment.carrentalservice.service;

import com.infor.assignment.carrentalservice.handler.order.BasicOrderHandler;
import com.infor.assignment.carrentalservice.handler.order.OrderHandler;
import com.infor.assignment.carrentalservice.util.MessageType;
import org.springframework.beans.factory.annotation.Autowired;

// This is to Handle Order Operations. when new Operation or API introduced to Order flow we can add message-type here in the switch case
public abstract class AbstractOrderServiceHandler {

    @Autowired
    private OrderHandler orderHandler;

    protected BasicOrderHandler getHandler(MessageType messageType) {
        switch (messageType) {
            case CREATE_ORDER:
            case MODIFY_ORDER:
            case DELETE_ORDER:
            case GET_ORDER:
                return orderHandler;

            //TODO can add different handlers as needed in the future
            default:
                throw new IllegalStateException("Unexpected value: " + messageType);

        }
    }
}
