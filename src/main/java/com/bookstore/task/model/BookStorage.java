package com.bookstore.task.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "book-storage")
public class BookStorage
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_STORAGE_GENERATOR")
    @SequenceGenerator(name = "BOOK_STORAGE_GENERATOR", sequenceName = "BOOK_STORAGE_SEQ", allocationSize = 1)
    @Column
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String isbn;

    @Column
    private String category;

    @Column
    private Long amount;
}

