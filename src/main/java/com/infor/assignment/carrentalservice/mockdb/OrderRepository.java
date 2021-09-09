package com.infor.assignment.carrentalservice.mockdb;


import com.infor.assignment.carrentalservice.exception.NoAvailabilityException;
import com.infor.assignment.carrentalservice.exception.OrderDuplicateException;
import com.infor.assignment.carrentalservice.exception.OrderNotFound;
import com.infor.assignment.carrentalservice.exception.ServiceException;
import com.infor.assignment.carrentalservice.model.order.Order;
import com.infor.assignment.carrentalservice.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


//DUMMY DB LAYER
//TODO:: in real case, all of these methods replaced with ORM,JPA Framework or using Spring-Data
@Repository
public class OrderRepository {

    private Map<String, Order> orderInfoRepo = new ConcurrentHashMap<>();
    //just using unique autoIncrementId to mock auto increment id done by ORM Framework.
    private AtomicInteger index = new AtomicInteger(1000);

    @Autowired
    DateUtil dateUtil;


    public Order getOrderInfoByOrderId(String id) {
        Order order = orderInfoRepo.get(id);
        return order;
    }

    public List<Order> getOrdersInfoByUserIdAndPlateId(String userId, String plateId) {
        List<Order> orderList = orderInfoRepo.entrySet()
                .stream()
                .filter(entry -> (entry.getValue().getUserId().equals(userId) || entry.getValue().getPlateId().equals(plateId)))
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());
        return orderList;
    }

    public Order save(Order order) throws ServiceException {
        List<Order> existingOrders = orderInfoRepo.entrySet()
                .stream()
                .filter(entry -> (entry.getValue().getUserId().equals(order.getUserId()) && entry.getValue().getPlateId().equals(order.getPlateId())))
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());

        if (existingOrders != null && !existingOrders.isEmpty()) {
            Optional<Order> foundOverLap = existingOrders.stream().filter(existing -> dateUtil.isOverlapping(existing.getRentedPeriod(), order.getRentedPeriod())).findFirst();
            if (foundOverLap == null || foundOverLap.isEmpty()) {
                order.setOrderId(System.currentTimeMillis() + String.format("%05d", index.incrementAndGet()));
                orderInfoRepo.put(order.getOrderId(), order);
                return order;
            } else {
                Order existingOrder = foundOverLap.get();
                if (existingOrder.equals(order))
                    throw new OrderDuplicateException("Order is duplicated", existingOrder);
                throw new NoAvailabilityException("No Availability Found. Vehicle is already reserved", order.getUserId(), order.getPlateId(), foundOverLap.get().getRentedPeriod());
            }
        } else {
            order.setOrderId(System.currentTimeMillis() + String.format("%05d", index.incrementAndGet()));
            orderInfoRepo.put(order.getOrderId(), order);
            return order;
        }
    }


    public Order modify(Order order) throws OrderNotFound {
        Optional<Order> foundOrder = orderInfoRepo.entrySet().stream().filter(entry -> entry.getValue().getOrderId().equals(order.getOrderId())).map(entry -> entry.getValue()).findFirst();
        if (foundOrder.isPresent()) {
            orderInfoRepo.put(order.getOrderId(), order);
            return order;
        }
        throw new OrderNotFound("No Order Found", order.getOrderId());
    }

    public Order delete(String orderId) throws OrderNotFound {
        if (orderInfoRepo.containsKey(orderId)) {
            return orderInfoRepo.remove(orderId);
        }
        return null;
    }


}
