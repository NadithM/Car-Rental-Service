package com.infor.assignment.carrentalservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ApplicationConfig {


    @Value("${taskExecutor.corePoolSize.vehicleOperation}")
    private int vehicleOperationTaskExecutorCorePoolSize;
    @Value("${taskExecutor.maxPoolSize.vehicleOperation}")
    private int vehicleOperationTaskExecutorMaxPoolSize;
    @Value("${taskExecutor.keepAliveTime.vehicleOperation}")
    private int vehicleOperationTaskExecutorKeepAliveTime;

    @Value("${taskExecutor.corePoolSize.userOperation}")
    private int userOperationTaskExecutorCorePoolSize;
    @Value("${taskExecutor.maxPoolSize.userOperation}")
    private int userOperationTaskExecutorMaxPoolSize;
    @Value("${taskExecutor.keepAliveTime.userOperation}")
    private int userOperationTaskExecutorKeepAliveTime;

    @Value("${taskExecutor.corePoolSize.orderOperation}")
    private int orderOperationTaskExecutorCorePoolSize;
    @Value("${taskExecutor.maxPoolSize.orderOperation}")
    private int orderOperationTaskExecutorMaxPoolSize;
    @Value("${taskExecutor.keepAliveTime.orderOperation}")
    private int orderOperationTaskExecutorKeepAliveTime;

    @Value("${taskExecutor.corePoolSize.searchOperation}")
    private int searchOperationTaskExecutorCorePoolSize;
    @Value("${taskExecutor.maxPoolSize.searchOperation}")
    private int searchOperationTaskExecutorMaxPoolSize;
    @Value("${taskExecutor.keepAliveTime.searchOperation}")
    private int searchOperationTaskExecutorKeepAliveTime;


    // task executors for separate flows namely,VehicleOperation,UserOperation,OrderOperation,SearchOperation
    // this will handle overwhelming requests from any flows independently, So overall system performance won't be decreases.
    // request will be queued until it serve. tomcat thread will be immediately released as soon as request submit it to the MessageChannel.
    // MessageChannel will route the request to corresponding thread pool task executor below.
    @Bean
    public ThreadPoolExecutor vehicleOperationTaskExecutor() {
        return new ThreadPoolExecutor(vehicleOperationTaskExecutorCorePoolSize, vehicleOperationTaskExecutorMaxPoolSize, vehicleOperationTaskExecutorKeepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Bean
    public ThreadPoolExecutor userOperationTaskExecutor() {
        return new ThreadPoolExecutor(userOperationTaskExecutorCorePoolSize, userOperationTaskExecutorMaxPoolSize, userOperationTaskExecutorKeepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Bean
    public ThreadPoolExecutor orderOperationTaskExecutor() {
        return new ThreadPoolExecutor(orderOperationTaskExecutorCorePoolSize, orderOperationTaskExecutorMaxPoolSize, orderOperationTaskExecutorKeepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Bean
    public ThreadPoolExecutor searchOperationTaskExecutor() {
        return new ThreadPoolExecutor(searchOperationTaskExecutorCorePoolSize, searchOperationTaskExecutorMaxPoolSize, searchOperationTaskExecutorKeepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

}