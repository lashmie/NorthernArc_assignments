package org.northernarc.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookRequestDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @Min(0)
    private Integer quantity;
}
