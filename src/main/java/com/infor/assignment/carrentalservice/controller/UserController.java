package com.infor.assignment.carrentalservice.controller;

import com.infor.assignment.carrentalservice.handler.RestRequestHandler;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.model.user.UserRequest;
import com.infor.assignment.carrentalservice.model.user.UserRequestCriteria;
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
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private RestRequestHandler restRequestHandler;

    @GetMapping
    public DeferredResult<ResponseEntity<?>> getUser(@RequestParam(value = Constants.USER_ID, required = false) String userId, @RequestParam(value = Constants.ID, required = false) String id) throws Exception {
        String requestId = UUID.randomUUID().toString();
        UserRequestCriteria userRequestCriteria = new UserRequestCriteria();
        userRequestCriteria.setRequestId(requestId);
        userRequestCriteria.setId(id);
        userRequestCriteria.setUserId(userId);
        Message<BasicRequestCriteria> lookupRequestCriteriaMessage = restRequestHandler.handleRequest(MessageType.GET_USER, userRequestCriteria);
        return lookupRequestCriteriaMessage.getPayload().getDeferredResult();
    }

    @PostMapping
    public DeferredResult<ResponseEntity<?>> registerUser(@Valid @RequestBody UserRequest userRequest) throws Exception {
        String requestId = UUID.randomUUID().toString();
        UserRequestCriteria userRequestCriteria = new UserRequestCriteria();
        userRequestCriteria.setRequestId(requestId);
        userRequestCriteria.setUserRequest(userRequest);
        Message<BasicRequestCriteria> lookupRequestCriteriaMessage = restRequestHandler.handleRequest(MessageType.REGISTER_USER, userRequestCriteria);
        return lookupRequestCriteriaMessage.getPayload().getDeferredResult();
    }
}
