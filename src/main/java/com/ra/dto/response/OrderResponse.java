package com.ra.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
   private  String userName;
   private List<OrderDetailResponse> orderDetailResponses;
}
