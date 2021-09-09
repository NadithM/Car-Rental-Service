package com.infor.assignment.carrentalservice.exception;

public class OrderNotFound extends ServiceException {
    private String orderId;

    public OrderNotFound(String message, String orderId) {
        super(message);
        this.orderId = orderId;
    }
}
