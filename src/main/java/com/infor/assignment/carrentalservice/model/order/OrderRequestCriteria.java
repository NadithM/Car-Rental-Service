package com.infor.assignment.carrentalservice.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderRequestCriteria extends BasicRequestCriteria {
    private OrderRequest orderRequest;
    private String userId;
}
