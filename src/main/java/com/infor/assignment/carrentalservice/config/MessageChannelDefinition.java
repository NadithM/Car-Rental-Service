package com.infor.assignment.carrentalservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class MessageChannelDefinition {


    @Autowired
    private ThreadPoolExecutor vehicleOperationTaskExecutor;
    @Autowired
    private ThreadPoolExecutor searchOperationTaskExecutor;
    @Autowired
    private ThreadPoolExecutor orderOperationTaskExecutor;
    @Autowired
    private ThreadPoolExecutor userOperationTaskExecutor;

    // each channel will be bind to task executor where each message will be processed in these task executors
    @Bean
    public MessageChannel commonMessagingChannel() {
        return new ExecutorChannel(vehicleOperationTaskExecutor);
    }

    @Bean
    public MessageChannel defaultCommonVehicleRegisterChannel() {
        return new ExecutorChannel(vehicleOperationTaskExecutor);
    }

    @Bean
    public MessageChannel defaultCommonVehicleInfoRetrieveChannel() {
        return new ExecutorChannel(vehicleOperationTaskExecutor);
    }

    @Bean
    public MessageChannel defaultCommonVehicleSearchChannel() {
        return new ExecutorChannel(searchOperationTaskExecutor);
    }

    @Bean
    public MessageChannel defaultCommonVehicleRegisterAvailabilityChannel() {
        return new ExecutorChannel(vehicleOperationTaskExecutor);
    }

    @Bean
    public MessageChannel userManageChannel() {
        return new ExecutorChannel(userOperationTaskExecutor);
    }

    @Bean
    public MessageChannel orderManagementChannel() {
        return new ExecutorChannel(orderOperationTaskExecutor);
    }

}
