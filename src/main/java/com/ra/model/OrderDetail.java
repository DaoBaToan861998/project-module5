package com.ra.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
