package com.infor.assignment.carrentalservice.mockdb;

import com.infor.assignment.carrentalservice.exception.DuplicateUserFoundException;
import com.infor.assignment.carrentalservice.exception.UserNotFound;
import com.infor.assignment.carrentalservice.model.user.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

//DUMMY DB LAYER
//TODO:: in real case, all of these methods replaced with ORM,JPA Framework or using Spring-Data
@Repository
public class UserRepository {

    private Map<String, User> userInfoRepo = new ConcurrentHashMap<>();
    //just using unique autoIncrementId to mock auto increment id done by ORM Framework.
    private AtomicInteger index = new AtomicInteger(1000);


    public User getUserInfoById(String id) {
        User user = userInfoRepo.get(id);
        return user;
    }

    public User getUserInfoByUserId(String userId) {
        Optional<User> foundUser = userInfoRepo.entrySet().stream().filter(entry -> entry.getValue().getUserId().equals(userId)).map(entry -> entry.getValue()).findFirst();
        if (foundUser.isPresent()) return foundUser.get();
        return null;
    }

    public User save(User user) {
        Optional<User> foundUser = userInfoRepo.entrySet().stream().filter(entry -> entry.getValue().getUserId().equals(user.getUserId())).map(entry -> entry.getValue()).findFirst();
        if (!foundUser.isPresent()) {
            user.setId(System.currentTimeMillis() + String.format("%05d", index.incrementAndGet()));
            userInfoRepo.put(user.getId(), user);
            return user;
        }
        throw new DuplicateUserFoundException("Duplicate Record Found");
    }

    public User modify(User user) throws UserNotFound {
        Optional<User> foundUser = userInfoRepo.entrySet().stream().filter(entry -> entry.getValue().getId().equals(user.getId())).map(entry -> entry.getValue()).findFirst();
        if (foundUser.isPresent()) {
            userInfoRepo.put(user.getId(), user);
            return user;
        }
        throw new UserNotFound("No Record Found", user.getId(), user.getUserId());
    }


}
