package com.infor.assignment.carrentalservice.handler.user;


import com.infor.assignment.carrentalservice.exception.UserNotFound;
import com.infor.assignment.carrentalservice.mockdb.UserRepository;
import com.infor.assignment.carrentalservice.model.user.User;
import com.infor.assignment.carrentalservice.model.user.UserRequest;
import com.infor.assignment.carrentalservice.model.user.UserRequestCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserHandler extends BasicUserHandler<UserRequestCriteria, User> {

    @Autowired
    private UserRepository userRepository;


    @Override
    public synchronized User registerUser(UserRequestCriteria userRequestCriteria) {
        UserRequest userRequest = userRequestCriteria.getUserRequest();
        User user = new User();
        user.setUserId(userRequest.getUserId());
        user.setEmail(userRequest.getEmail());
        user.setFName(userRequest.getFName());
        user.setLName(userRequest.getLName());
        user.setPassword(userRequest.getPassword());
        return userRepository.save(user);
    }

    @Override
    public User retrieveUser(UserRequestCriteria userRequestCriteria) throws UserNotFound {
        User userInfo = null;
        if (userRequestCriteria.getId() != null) {
            userInfo = userRepository.getUserInfoById(userRequestCriteria.getId());
        } else if (userRequestCriteria.getUserId() != null) {
            userInfo = userRepository.getUserInfoByUserId(userRequestCriteria.getUserId());
        }
        if (userInfo == null) {
            throw new UserNotFound("Requested User is not found", userRequestCriteria.getId(), userRequestCriteria.getUserId());
        }
        return userInfo;
    }
}
