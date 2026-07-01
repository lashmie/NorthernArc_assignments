package org.northernarc.ecomapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<Order> orders;

}
