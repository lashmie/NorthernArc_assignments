package org.northernarc.application.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private Integer quantity;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BorrowRecord> borrowRecords;
}
