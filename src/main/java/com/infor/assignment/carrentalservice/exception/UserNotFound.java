package com.infor.assignment.carrentalservice.exception;

public class UserNotFound extends ServiceException {
    private String id;
    private String userId;

    public UserNotFound(String message, String id, String userId) {
        super(message);
        this.id = id;
        this.userId = userId;
    }
}
