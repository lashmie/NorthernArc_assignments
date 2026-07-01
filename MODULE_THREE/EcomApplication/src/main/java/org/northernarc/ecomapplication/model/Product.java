package org.northernarc.ecomapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name="Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private String category;
    private double cost;

    @OneToMany(mappedBy = "product",cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Order_Item> orderItems;
}
