package com.ra.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrder {
    private String addressShip;
    private String phone;
    @JsonFormat(pattern = "dd/MM/yy")
    private Date createAt;
    private String note;
}
