package com.infor.assignment.carrentalservice.util;

public enum MessageType {

    REGISTER_CAR("REGCAR", "Register Car"),
    REGISTER_USER("REGUSR", "Register User"),
    REGISTER_AVAILABILITY_CAR("REGAVLCAR", "Register Availability Car"),
    SEARCH_AVAILABLE_CARS("SRCAVLCAR", "Search Available Cars"),
    GET_CAR("GETCAR", "Retrieve Car"),
    GET_USER("GETUSR", "Retrieve User"),
    CREATE_ORDER("CRTORDVHI", "Create Order"),
    GET_ORDER("GETORDVHI", "Retrieve Order"),
    DELETE_ORDER("DELORDVHI", "Delete Order"),
    MODIFY_ORDER("MDYORDVHI", "Modify Order");

    private String messageType;
    private String name;

    MessageType(String messageType, String name) {
        this.messageType = messageType;
        this.name = name;
    }
}
