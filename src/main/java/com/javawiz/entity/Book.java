package com.javawiz.entity;

import com.javawiz.validator.Author;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty(message = "Please provide a name")
    private String name;
    @Author
    @NotEmpty(message = "Please provide a author")
    private String author;
    @NotNull(message = "Please provide a price")
    @DecimalMin("1.00")
    private BigDecimal price;

    public Book(Long id, String name, String author, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
    }
}
