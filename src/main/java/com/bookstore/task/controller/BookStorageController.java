package com.bookstore.task.controller;

import com.bookstore.task.controller.dto.BookStorageDto;
import com.bookstore.task.service.BookStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/book-storage")
@RequiredArgsConstructor
@CrossOrigin
public class BookStorageController
{
    private final BookStorageService bookStorageService;

    @GetMapping
    public List<BookStorageDto> getAllBooks() {
        return bookStorageService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookStorageDto> getBookById(@PathVariable Long id) {
        return bookStorageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookStorageDto> create(@Valid @RequestBody BookStorageDto bookStorageDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookStorageService.save(bookStorageDto));
    }

    @GetMapping("/search")
    public Page<BookStorageDto> searchBooks(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String isbn,
        @RequestParam(required = false) String category,
        @RequestParam(required = false, defaultValue = "title") String sortOrder,
        @RequestParam(required = false, defaultValue = "true") boolean isAscending,
        @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
        @RequestParam(required = false, defaultValue = "25") Integer pageSize
    ) {
        Sort sorted = isAscending ? Sort.by(sortOrder).ascending() : Sort.by(sortOrder).descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sorted);
        return bookStorageService.search(title, author, isbn, category, pageRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookStorageDto> update(@PathVariable Long id, @Valid @RequestBody BookStorageDto bookDetails) {
        return ResponseEntity.ok(bookStorageService.save(bookDetails));
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookStorageService.deleteById(id);
    }

    @PostMapping("/borrow/{id}")
    public ResponseEntity<BookStorageDto> borrowBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookStorageService.borrowBook(id));
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<BookStorageDto> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookStorageService.returnBook(id));
    }
}
