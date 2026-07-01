package org.northernarc.flightapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="FlightTicket")
public class FlightTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;
    private Integer seatNo;
    private LocalDate bookingDate;
//    @ManyToOne
//    private Customer customer;
//    @ManyToOne
//    private Flight flight;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
}
