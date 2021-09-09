package com.infor.assignment.carrentalservice.model.common;

import com.infor.assignment.carrentalservice.util.Constants;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Builder(setterPrefix = "set")
@Getter
public class ErrorResponse {

    private HttpStatus status;
    private String errorInfo;
    private String location;

    public Map<String, String> getErrorInfo() {
        Map<String, String> errorMap = new HashMap<>();
        //default error response
        if (errorInfo != null)
            errorMap.put(Constants.ERROR_INFO, errorInfo);
        if (location != null)
            errorMap.put(Constants.LOCATION, location);
        if (status != null)
            errorMap.put(Constants.STATUS, String.valueOf(status.value()));
        return errorMap;
    }

}

