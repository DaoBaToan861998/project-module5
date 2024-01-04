package com.ra.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.model.OrderStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListOrder {
    private Long ordersId;
    private  String addressShip;
    private String phone;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createAt;
    private Boolean delivery;
    private String note;
    private String userName;
    private OrderStatus orderStatus;
}
