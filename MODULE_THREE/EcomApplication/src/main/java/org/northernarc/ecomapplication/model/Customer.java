package org.northernarc.ecomapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="ecom_customer")
public class Customer {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "customer_id")
    private Long id;
    private String name;
    @Email
    private String email;
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

}
