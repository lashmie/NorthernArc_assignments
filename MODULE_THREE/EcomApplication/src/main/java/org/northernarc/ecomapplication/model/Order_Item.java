package org.northernarc.ecomapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ecom_order_item")
public class Order_Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="orderitem_id")
    private Long id;
    private int quantity;

    @ManyToOne
    @JsonBackReference
    private Product product;

    @OneToMany
    @JsonManagedReference
    private Order order;



}
