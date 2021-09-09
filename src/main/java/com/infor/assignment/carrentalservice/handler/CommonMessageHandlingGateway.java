package com.infor.assignment.carrentalservice.handler;

import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

// accept the request from tomcat servlet thread and routing message to relevant thread pool.
@MessagingGateway(
        name = "commonMessageHandlingGateway",
        defaultRequestChannel = "commonMessagingChannel")
@Component
public interface CommonMessageHandlingGateway {

    @Gateway
    void send(Message<? extends BasicRequestCriteria> xbInputMessage);

}