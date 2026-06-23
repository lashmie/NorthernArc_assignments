package org.northernarc.ecomapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@Table(name="ecom_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;
    private Date orderDate;
    private Date delvieryDate;

    @ManyToOne
    @JsonBackReference
    private Customer customer;

    @OneToMany
    @JsonManagedReference
    private List<Order> Order_Item;

}
