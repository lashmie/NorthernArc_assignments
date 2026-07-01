package org.northernarc.productrental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Product name cannot be empty")
    private String productName;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    @NotNull
    @PositiveOrZero(message = "Rent cannot be negative")
    private Double rentPerDay;

    @NotNull
    private Boolean available;
    @JsonIgnore
    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<RentalRecord> rentalRecords = new ArrayList<>();


}