package com.infor.assignment.carrentalservice.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserRequestCriteria extends BasicRequestCriteria {
    private UserRequest userRequest;
    private String userId;
}
