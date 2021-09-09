package com.infor.assignment.carrentalservice.exception;

import com.infor.assignment.carrentalservice.model.order.Order;
import lombok.Getter;

@Getter
public class OrderDuplicateException extends ServiceException {

    private Order order;

    public OrderDuplicateException(String message, Order order) {
        super(message);
        this.order = order;
    }
}
