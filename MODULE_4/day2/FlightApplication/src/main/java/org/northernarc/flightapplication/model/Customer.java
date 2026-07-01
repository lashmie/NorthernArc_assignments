package org.northernarc.flightapplication.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Flight_customer_table")
public class Customer {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long customerId;
    private String phone;
    private String name;
    private String email;
    private String address;
    private String passportNo;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private java.util.List<FlightTicket> bookings;

}
