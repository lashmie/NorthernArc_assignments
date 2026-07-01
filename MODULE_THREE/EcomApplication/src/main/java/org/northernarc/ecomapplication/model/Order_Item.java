package org.northernarc.ecomapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Product product;

    @ManyToOne
    @JsonIgnore
    private Order order;



}
