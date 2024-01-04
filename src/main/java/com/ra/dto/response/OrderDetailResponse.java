package com.ra.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
   private Long id;
    private String productName;
    private Double price;
    private int quantity;


}
