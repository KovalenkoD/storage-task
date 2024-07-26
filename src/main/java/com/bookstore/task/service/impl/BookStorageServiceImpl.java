package com.bookstore.task.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bookstore.task.controller.dto.BookStorageDto;
import com.bookstore.task.mapper.BookStorageMapper;
import com.bookstore.task.model.BookStorage;
import com.bookstore.task.repository.BookStorageRepository;
import com.bookstore.task.service.BookStorageService;
import com.bookstore.task.specifications.BookSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookStorageServiceImpl implements BookStorageService
{
    private final BookStorageMapper bookStorageMapper;
    private final BookStorageRepository bookStorageRepository;

    @Override
    public List<BookStorageDto> findAll() {
       return bookStorageRepository.findAll().stream()
                .map(bookStorageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookStorageDto> findById(Long id) {
        return bookStorageRepository.findById(id).map(bookStorageMapper::toDto);
    }

    @Override
    public BookStorageDto save(BookStorageDto bookStorageDto) {
        var bookStorage = bookStorageMapper.toEntity(bookStorageDto);
        return bookStorageMapper.toDto(bookStorageRepository.save(bookStorage));
    }

    @Override
    public void deleteById(Long id) {
        bookStorageRepository.deleteById(id);
    }

    @Override
    public BookStorageDto borrowBook(Long id) {
        var bookStorage = bookStorageRepository.findById(id).orElseThrow();
        if (bookStorage.getAmount() <= 0) {
            throw new RuntimeException("No Available books");
        }
        bookStorage.setAmount(bookStorage.getAmount() - 1);
        return bookStorageMapper.toDto(bookStorageRepository.save(bookStorage));
    }

    @Override
    public BookStorageDto returnBook(Long id) {
        var bookStorage = bookStorageRepository.findById(id).orElseThrow();
        bookStorage.setAmount(bookStorage.getAmount() + 1);
        return bookStorageMapper.toDto(bookStorageRepository.save(bookStorage));
    }

    @Override
    public Page<BookStorageDto> search(String title, String author, String isbn, String category, PageRequest pageRequest)
    {
        Specification<BookStorage> spec = Specification.where(BookSpecification.by(title, "title"))
            .and(BookSpecification.by(author, "author"))
            .and(BookSpecification.by(isbn, "isbn"))
            .and(BookSpecification.by(category, "category"));
        return bookStorageRepository.findAll(spec, pageRequest).map(bookStorageMapper::toDto);
    }
}
