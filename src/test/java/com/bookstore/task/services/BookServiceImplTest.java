package com.bookstore.task.services;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookstore.task.controller.dto.BookStorageDto;
import com.bookstore.task.mapper.BookStorageMapper;
import com.bookstore.task.model.BookStorage;
import com.bookstore.task.repository.BookStorageRepository;
import com.bookstore.task.service.impl.BookStorageServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class BookServiceImplTest {

    @Mock
    private BookStorageMapper bookStorageMapper;

    @Mock
    private BookStorageRepository bookStorageRepository;

    private BookStorageServiceImpl bookStorageService;

    private BookStorage bookStorage;
    private BookStorageDto bookStorageDto;

    @BeforeEach
    void setUp() {
        bookStorageService = new BookStorageServiceImpl(bookStorageMapper, bookStorageRepository);

        bookStorage = new BookStorage();
        bookStorage.setId(1L);
        bookStorage.setTitle("Test Book");
        bookStorage.setAuthor("Test Author");
        bookStorage.setAmount(2l);

        bookStorageDto = new BookStorageDto();
        bookStorageDto.setId(1L);
        bookStorageDto.setTitle("Test Book");
        bookStorageDto.setAuthor("Test Author");
    }

    @Test
    void findAll_shouldReturnAllBooks() {
        when(bookStorageRepository.findAll()).thenReturn(List.of(bookStorage));
        when(bookStorageMapper.toDto(any(BookStorage.class))).thenReturn(bookStorageDto);

        var books = bookStorageService.findAll();

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Test Book");

        verify(bookStorageRepository, times(1)).findAll();
        verify(bookStorageMapper, times(1)).toDto(any(BookStorage.class));
    }

    @Test
    void findById_shouldReturnBook_whenBookExists() {
        when(bookStorageRepository.findById(anyLong())).thenReturn(Optional.of(bookStorage));
        when(bookStorageMapper.toDto(any(BookStorage.class))).thenReturn(bookStorageDto);

        var foundBook = bookStorageService.findById(1L);

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Test Book");

        verify(bookStorageRepository, times(1)).findById(anyLong());
        verify(bookStorageMapper, times(1)).toDto(any(BookStorage.class));
    }

    @Test
    void findById_shouldReturnEmpty_whenBookDoesNotExist() {
        when(bookStorageRepository.findById(anyLong())).thenReturn(Optional.empty());

        var foundBook = bookStorageService.findById(1L);

        assertThat(foundBook).isNotPresent();

        verify(bookStorageRepository, times(1)).findById(anyLong());
        verify(bookStorageMapper, never()).toDto(any(BookStorage.class));
    }

    @Test
    void save_shouldSaveAndReturnBook() {
        when(bookStorageMapper.toEntity(any(BookStorageDto.class))).thenReturn(bookStorage);
        when(bookStorageRepository.save(any(BookStorage.class))).thenReturn(bookStorage);
        when(bookStorageMapper.toDto(any(BookStorage.class))).thenReturn(bookStorageDto);

        var savedBook = bookStorageService.save(bookStorageDto);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("Test Book");

        verify(bookStorageMapper, times(1)).toEntity(any(BookStorageDto.class));
        verify(bookStorageRepository, times(1)).save(any(BookStorage.class));
        verify(bookStorageMapper, times(1)).toDto(any(BookStorage.class));
    }

    @Test
    void deleteById_shouldDeleteBook() {
        doNothing().when(bookStorageRepository).deleteById(anyLong());

        bookStorageService.deleteById(1L);

        verify(bookStorageRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void borrowBook_shouldMarkBookAsBorrowed_whenBookExistsAndNotBorrowed() {
        when(bookStorageRepository.findById(anyLong())).thenReturn(Optional.of(bookStorage));
        when(bookStorageRepository.save(any(BookStorage.class))).thenReturn(bookStorage);
        when(bookStorageMapper.toDto(any(BookStorage.class))).thenReturn(bookStorageDto);

        var borrowedBook = bookStorageService.borrowBook(1L);

        assertThat(borrowedBook).isNotNull();

        verify(bookStorageRepository, times(1)).findById(anyLong());
        verify(bookStorageRepository, times(1)).save(any(BookStorage.class));
        verify(bookStorageMapper, times(1)).toDto(any(BookStorage.class));
    }

    @Test
    void borrowBook_shouldThrowException_whenBookDoesNotExist() {
        when(bookStorageRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookStorageService.borrowBook(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No value present");

        verify(bookStorageRepository, times(1)).findById(anyLong());
        verify(bookStorageRepository, never()).save(any(BookStorage.class));
        verify(bookStorageMapper, never()).toDto(any(BookStorage.class));
    }

    @Test
    void borrowBook_shouldThrowException_whenBookIsAlreadyBorrowed() {
        bookStorage.setAmount(0l);
        when(bookStorageRepository.findById(anyLong())).thenReturn(Optional.of(bookStorage));

        assertThatThrownBy(() -> bookStorageService.borrowBook(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No Available books");

        verify(bookStorageRepository, times(1)).findById(anyLong());
        verify(bookStorageRepository, never()).save(any(BookStorage.class));
        verify(bookStorageMapper, never()).toDto(any(BookStorage.class));
    }

    @Test
    void returnBook_shouldMarkBookAsNotBorrowed_whenBookExistsAndBorrowed() {
        bookStorage.setAmount(2l);
        when(bookStorageRepository.findById(anyLong())).thenReturn(Optional.of(bookStorage));
        when(bookStorageRepository.save(any(BookStorage.class))).thenReturn(bookStorage);
        when(bookStorageMapper.toDto(any(BookStorage.class))).thenReturn(bookStorageDto);

        var returnedBook = bookStorageService.returnBook(1L);

        assertThat(returnedBook).isNotNull();

        verify(bookStorageRepository, times(1)).findById(anyLong());
        verify(bookStorageRepository, times(1)).save(any(BookStorage.class));
        verify(bookStorageMapper, times(1)).toDto(any(BookStorage.class));
    }

    @Test
    void returnBook_shouldThrowException_whenBookDoesNotExist() {
        when(bookStorageRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookStorageService.returnBook(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No value present");

        verify(bookStorageRepository, times(1)).findById(anyLong());
        verify(bookStorageRepository, never()).save(any(BookStorage.class));
        verify(bookStorageMapper, never()).toDto(any(BookStorage.class));
    }
}

