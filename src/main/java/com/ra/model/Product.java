package com.ra.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Double price;
    private String description;
    private Boolean status;

    private int stock;

     private String image;


//    @OneToMany(mappedBy = "product")
//    private List<OrderDetail> orderDetails;

    @JsonIgnore
//    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "categoryId",referencedColumnName = "categoryId")
    private Category category;

}
