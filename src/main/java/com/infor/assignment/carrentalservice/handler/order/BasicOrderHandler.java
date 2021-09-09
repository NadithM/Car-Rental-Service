package com.infor.assignment.carrentalservice.handler.order;


import com.infor.assignment.carrentalservice.exception.OrderNotFound;
import com.infor.assignment.carrentalservice.exception.PriceMismatchException;
import com.infor.assignment.carrentalservice.exception.UserNotFound;
import com.infor.assignment.carrentalservice.exception.VehicleNotFound;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.model.order.Order;
import org.springframework.stereotype.Component;

@Component
public abstract class BasicOrderHandler<T extends BasicRequestCriteria, O extends Order> {

    public abstract O getOrder(T basicOrderCriteria) throws OrderNotFound;

    public abstract O createOrder(T basicOrderCriteria) throws VehicleNotFound, PriceMismatchException, UserNotFound, Exception;

    public abstract O modifyOrder(T basicOrderCriteria) throws OrderNotFound, VehicleNotFound, PriceMismatchException;

    public abstract O deleteOrder(T basicOrderCriteria) throws OrderNotFound, VehicleNotFound;
}
