package com.infor.assignment.carrentalservice.route;

import com.infor.assignment.carrentalservice.util.MessageHeaders;
import com.infor.assignment.carrentalservice.util.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;


// THis class will route the message to relevant service class to process the request.
@Component
public class MessageRouter {

    @Autowired
    MessageChannel defaultCommonVehicleRegisterChannel;

    @Autowired
    MessageChannel defaultCommonVehicleInfoRetrieveChannel;

    @Autowired
    MessageChannel defaultCommonVehicleSearchChannel;

    @Autowired
    MessageChannel defaultCommonVehicleRegisterAvailabilityChannel;

    @Autowired
    MessageChannel userManageChannel;

    @Autowired
    MessageChannel orderManagementChannel;

    @Router(inputChannel = "commonMessagingChannel")
    public MessageChannel globalMessageRoutingIn(@Header(MessageHeaders.MESSAGE_TYPE) MessageType messageType) {
        MessageChannel messageChannel = null;
        switch (messageType) {
            case REGISTER_CAR:
                //TODO: Add more message type here to register,it will be handle by the default flow
                messageChannel = defaultCommonVehicleRegisterChannel;
                break;
            case SEARCH_AVAILABLE_CARS:
                //TODO: Add more message type here to search,it will be handle by the default flow
                messageChannel = defaultCommonVehicleSearchChannel;
                break;
            case GET_CAR:
                //TODO: Add more message type here to retrieve,it will be handle by the default flow
                messageChannel = defaultCommonVehicleInfoRetrieveChannel;
                break;
            case REGISTER_AVAILABILITY_CAR:
                //TODO: Add more message type here to registerAvailability,it will be handle by the default flow
                messageChannel = defaultCommonVehicleRegisterAvailabilityChannel;
                break;


            case REGISTER_USER:
            case GET_USER:
                //TODO: Add more message type here to do something with users,like MODIFY_USER,DELETE_USER,it will be handle by the default flow
                messageChannel = userManageChannel;
                break;

            case CREATE_ORDER:
            case GET_ORDER:
                //TODO: Add more message type here to do something with users,like MODIFY_ORDER,DELETE_ORDER,it will be handle by the default flow
                messageChannel = orderManagementChannel;
                break;
        }
        return messageChannel;
    }
}
