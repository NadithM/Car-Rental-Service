package com.infor.assignment.carrentalservice.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {

    @JsonProperty("userId")
    @NotEmpty(message = "userId can't be null or empty")
    private String userId;
    @JsonProperty("fName")
    @NotEmpty(message = "fName can't be null or empty")
    private String fName;
    @JsonProperty("lName")
    @NotEmpty(message = "lName can't be null or empty")
    private String lName;
    @JsonProperty("email")
    @NotEmpty(message = "email can't be null or empty")
    private String email;
    @JsonProperty("password")
    @NotEmpty(message = "password can't be null or empty")
    private String password;

}
