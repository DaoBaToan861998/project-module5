package com.ra.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String addressShip;
    private String phone;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createAt;

//    phân biệt trạn thái giỏ hàng hay đơn hàng delivery
    private  Boolean delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String note;


//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "orders")
    private List<OrderDetail> orderDetails;





}
