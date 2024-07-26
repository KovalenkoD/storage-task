package com.bookstore.task.service;

import com.bookstore.task.controller.dto.BookStorageDto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface BookStorageService
{
    List<BookStorageDto> findAll();
    Optional<BookStorageDto> findById(Long id);
    BookStorageDto save(BookStorageDto bookStorageDto);
    void deleteById(Long id);
    BookStorageDto borrowBook(Long id);
    BookStorageDto returnBook(Long id);
    Page<BookStorageDto> search(String title, String author, String isbn, String category, PageRequest pageRequest);
}
