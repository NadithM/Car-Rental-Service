package com.infor.assignment.carrentalservice.handler.order;


import com.infor.assignment.carrentalservice.exception.*;
import com.infor.assignment.carrentalservice.mockdb.OrderRepository;
import com.infor.assignment.carrentalservice.mockdb.UserRepository;
import com.infor.assignment.carrentalservice.mockdb.VehicleRepository;
import com.infor.assignment.carrentalservice.model.common.DateRange;
import com.infor.assignment.carrentalservice.model.order.Order;
import com.infor.assignment.carrentalservice.model.order.OrderRequest;
import com.infor.assignment.carrentalservice.model.order.OrderRequestCriteria;
import com.infor.assignment.carrentalservice.model.user.User;
import com.infor.assignment.carrentalservice.model.vehicle.Vehicle;
import com.infor.assignment.carrentalservice.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class OrderHandler extends BasicOrderHandler<OrderRequestCriteria, Order> {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    DateUtil dateUtil;


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    // @Transactional : This is important to manage transactions atomicity. if the database is there,We can Enable Transaction Management and use this Isolation levels

    public synchronized Order createOrder(OrderRequestCriteria orderRequestCriteria) throws ServiceException {
        OrderRequest orderRequest = orderRequestCriteria.getOrderRequest();
        //once we get the vehicle object,records will be on read/write lock until commits again
        Vehicle vehicle = vehicleRepository.getVehicleInfoByPlateId(orderRequest.getPlateId());
        if (vehicle == null)
            throw new VehicleNotFound("Plate Number is Invalid :" + orderRequest.getPlateId(), null, orderRequest.getPlateId());
        if (vehicle != null && !vehicle.isActive())
            throw new VehicleNotFound("Vehicle Temporary out of service :" + orderRequest.getPlateId(), null, orderRequest.getPlateId());

        User userInfo = userRepository.getUserInfoByUserId(orderRequest.getUserId());
        if (userInfo == null)
            throw new UserNotFound("User Id is Invalid :" + orderRequest.getUserId(), null, orderRequest.getUserId());

        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setPlateId(orderRequest.getPlateId());
        order.setRentedPeriod(orderRequest.getRentedPeriod());
        if (orderRequest.getRentedRatePerHour() != null && orderRequest.getRentedRatePerHour().equals(vehicle.getRentalPricePerHour())) {
            order.setRentedRatePerHour(vehicle.getRentalPricePerHour());
        } else if (orderRequest.getRentedRatePerHour() == null) {
            order.setRentedRatePerHour(vehicle.getRentalPricePerHour());
        } else {
            throw new PriceMismatchException("Price mismatch in the order and the vehicle");
        }
        order.setCreatedAt(LocalDateTime.now());
        order.setModifiedAt(LocalDateTime.now());
        Order save = null;
        try {
            save = orderRepository.save(order);
        } catch (OrderDuplicateException e) {
            return e.getOrder();
        }


        //checking availability is valid
        Optional<DateRange> foundAvailability = vehicle.getAvailableDates().stream().filter(availDateRange -> dateUtil.isWithinRange(availDateRange, order.getRentedPeriod())).findFirst();
        if (!foundAvailability.isPresent()) {
            throw new NoAvailabilityException("No availability for the order", order.getUserId(), order.getPlateId(), orderRequest.getRentedPeriod());
        }

        Map<String, DateRange> blackOutDatesByOrder = vehicle.getBlackOutDatesByOrder();
        if (blackOutDatesByOrder == null) {
            blackOutDatesByOrder = new LinkedHashMap<>();
        }
        Optional<DateRange> foundOverlapping = blackOutDatesByOrder.values().stream().filter(notAvailableDateRange -> dateUtil.isOverlapping(notAvailableDateRange, order.getRentedPeriod())).findFirst();
        if (foundOverlapping.isPresent()) {
            throw new NoAvailabilityException("No availability for the order", order.getUserId(), order.getPlateId(), orderRequest.getRentedPeriod());
        }
        blackOutDatesByOrder.put(save.getOrderId(), orderRequest.getRentedPeriod());
        vehicle.setBlackOutDatesByOrder(blackOutDatesByOrder);
        Vehicle modified = vehicleRepository.modify(vehicle);
        return save;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    // @Transactional : This is important to manage transactions atomicity. if the database is there,We can Enable Transaction Management and use this Isolation levels

    public synchronized Order modifyOrder(OrderRequestCriteria orderRequestCriteria) throws OrderNotFound, VehicleNotFound, PriceMismatchException {
        OrderRequest orderRequest = orderRequestCriteria.getOrderRequest();
        //in the real case, once we get, vehicle object will be locked by @Transactional Annotation
        Vehicle vehicle = vehicleRepository.getVehicleInfoByPlateId(orderRequest.getPlateId());

        Order orderInfo = null;
        if (orderRequestCriteria.getId() != null) {
            orderInfo = orderRepository.getOrderInfoByOrderId(orderRequestCriteria.getId());
        }
        if (orderInfo == null) {
            throw new OrderNotFound("Requested Order is not found", orderRequestCriteria.getId());
        }

        orderInfo.setModifiedAt(LocalDateTime.now());
        if (orderRequest.getRentedPeriod() != null) {
            orderInfo.setRentedPeriod(orderRequest.getRentedPeriod());
        }

        if (orderRequest.getRentedRatePerHour() != null && orderRequest.getRentedRatePerHour().equals(vehicle.getRentalPricePerHour())) {
            orderInfo.setRentedRatePerHour(vehicle.getRentalPricePerHour());
        } else if (!orderRequest.getRentedRatePerHour().equals(vehicle.getRentalPricePerHour())) {
            throw new PriceMismatchException("Price mismatch in the order and the vehicle");
        }


        //checking availability is valid
        Optional<DateRange> foundAvailability = vehicle.getAvailableDates().stream().filter(availDateRange -> dateUtil.isWithinRange(availDateRange, orderRequest.getRentedPeriod())).findFirst();
        if (!foundAvailability.isPresent()) {
            throw new NoAvailabilityException("No availability for the order", orderRequest.getUserId(), orderRequest.getPlateId(), orderRequest.getRentedPeriod());
        }

        Map<String, DateRange> blackOutDatesByOrder = vehicle.getBlackOutDatesByOrder();
        blackOutDatesByOrder.remove(orderInfo.getOrderId());

        Optional<DateRange> foundOverlapping = blackOutDatesByOrder.values().stream().filter(notAvailableDateRange -> dateUtil.isOverlapping(notAvailableDateRange, orderRequest.getRentedPeriod())).findFirst();
        if (foundOverlapping.isPresent()) {
            throw new NoAvailabilityException("No availability for the order", orderRequest.getUserId(), orderRequest.getPlateId(), orderRequest.getRentedPeriod());
        }
        blackOutDatesByOrder.put(orderInfo.getOrderId(), orderRequest.getRentedPeriod());
        vehicle.setBlackOutDatesByOrder(blackOutDatesByOrder);
        Vehicle modified = vehicleRepository.modify(vehicle);
        return orderInfo;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    // @Transactional : This is important to manage transactions atomicity. if the database is there,We can Enable Transaction Management and use this Isolation levels

    public synchronized Order deleteOrder(OrderRequestCriteria orderRequestCriteria) throws OrderNotFound, VehicleNotFound {
        OrderRequest orderRequest = orderRequestCriteria.getOrderRequest();
        Order orderInfo = null;
        //in the real case, once we get, vehicle object will be locked by @Transactional Annotation
        Vehicle vehicle = vehicleRepository.getVehicleInfoByPlateId(orderRequest.getPlateId());

        if (orderRequestCriteria.getId() != null) {
            orderInfo = orderRepository.getOrderInfoByOrderId(orderRequestCriteria.getId());
        }
        if (orderInfo == null) {
            throw new OrderNotFound("Requested Order is not found", orderRequestCriteria.getId());
        }
        Map<String, DateRange> blackOutDatesByOrder = vehicle.getBlackOutDatesByOrder();
        blackOutDatesByOrder.remove(orderInfo.getOrderId());
        vehicle.setBlackOutDatesByOrder(blackOutDatesByOrder);
        Vehicle modified = vehicleRepository.modify(vehicle);
        return orderRepository.delete(orderRequestCriteria.getId());
    }

    @Override
    public synchronized Order getOrder(OrderRequestCriteria orderRequestCriteria) throws OrderNotFound {
        Order orderInfo = null;
        if (orderRequestCriteria.getId() != null) {
            orderInfo = orderRepository.getOrderInfoByOrderId(orderRequestCriteria.getId());
        }
        if (orderInfo == null) {
            throw new OrderNotFound("Requested Order is not found", orderRequestCriteria.getId());
        }
        return orderInfo;
    }
}
