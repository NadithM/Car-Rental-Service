package com.infor.assignment.carrentalservice.controller;

import com.infor.assignment.carrentalservice.handler.RestRequestHandler;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.model.order.OrderRequest;
import com.infor.assignment.carrentalservice.model.order.OrderRequestCriteria;
import com.infor.assignment.carrentalservice.util.Constants;
import com.infor.assignment.carrentalservice.util.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/v1/orders")
public class OrderController {

    @Autowired
    private RestRequestHandler restRequestHandler;

    @GetMapping
    public DeferredResult<ResponseEntity<?>> getOrder(@RequestParam(value = Constants.ORDER_ID, required = false) String orderId) throws Exception {
        String requestId = UUID.randomUUID().toString();
        OrderRequestCriteria userRequestCriteria = new OrderRequestCriteria();
        userRequestCriteria.setRequestId(requestId);
        userRequestCriteria.setId(orderId);
        Message<BasicRequestCriteria> lookupRequestCriteriaMessage = restRequestHandler.handleRequest(MessageType.GET_ORDER, userRequestCriteria);
        return lookupRequestCriteriaMessage.getPayload().getDeferredResult();
    }

    @PostMapping
    public DeferredResult<ResponseEntity<?>> createOrder(@Valid @RequestBody OrderRequest orderRequest) throws Exception {
        String requestId = UUID.randomUUID().toString();
        OrderRequestCriteria userRequestCriteria = new OrderRequestCriteria();
        userRequestCriteria.setRequestId(requestId);
        userRequestCriteria.setOrderRequest(orderRequest);
        Message<BasicRequestCriteria> lookupRequestCriteriaMessage = restRequestHandler.handleRequest(MessageType.CREATE_ORDER, userRequestCriteria);
        return lookupRequestCriteriaMessage.getPayload().getDeferredResult();
    }
}
