package com.infor.assignment.carrentalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableTransactionManagement // no use case since DB is not there.
public class CarRentalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalServiceApplication.class, args);
    }

}
