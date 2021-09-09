package com.infor.assignment.carrentalservice.service;


import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public interface AbstractExecutorService {
    Message<Void> executeProcess(Message<? extends BasicRequestCriteria> basicCriteriaMessage);
}
