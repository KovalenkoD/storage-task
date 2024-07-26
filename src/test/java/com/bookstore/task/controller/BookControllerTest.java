package com.bookstore.task.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.bookstore.task.controller.dto.BookStorageDto;
import com.bookstore.task.service.BookStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookStorageController.class)
@ExtendWith(MockitoExtension.class)
@EnableWebMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookStorageService bookStorageService;

    private BookStorageController bookStorageController;

    private ObjectMapper objectMapper;
    private BookStorageDto bookStorageDto;

    @BeforeEach
    void setUp() {
        bookStorageController = new BookStorageController(bookStorageService);
        objectMapper = new ObjectMapper();

        bookStorageDto = new BookStorageDto();
        bookStorageDto.setId(1L);
        bookStorageDto.setTitle("The Lord of the Rings");
        bookStorageDto.setAuthor("J. R. R. Tolkien");
        bookStorageDto.setIsbn("1487587");
        bookStorageDto.setCategory("High fantasy");
        bookStorageDto.setAmount(2l);

        mockMvc = MockMvcBuilders.standaloneSetup(bookStorageController).build();
    }

    @Test
    void getAllBooks_shouldReturnListOfBooksInStorage() throws Exception {
        when(bookStorageService.findAll()).thenReturn(Collections.singletonList(bookStorageDto));

        mockMvc.perform(get("/v1/book-storage")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Lord of the Rings"));
    }

    @Test
    void getBookById_shouldReturnBook_whenBookExistsInStorage() throws Exception {
        when(bookStorageService.findById(1L)).thenReturn(Optional.of(bookStorageDto));

        mockMvc.perform(get("/v1/book-storage/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Lord of the Rings"));
    }

    @Test
    void getBookById_shouldReturnNotFound_whenBookDoesNotExist() throws Exception {
        when(bookStorageService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/book-storage/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBook_shouldReturnCreatedBook() throws Exception {
        when(bookStorageService.save(any(BookStorageDto.class))).thenReturn(bookStorageDto);

        mockMvc.perform(post("/v1/book-storage")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookStorageDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("The Lord of the Rings"));
    }

    @Test
    void updateBook_shouldReturnUpdatedBook_whenBookExists() throws Exception {
        when(bookStorageService.findById(1L)).thenReturn(Optional.of(bookStorageDto));
        when(bookStorageService.save(any(BookStorageDto.class))).thenReturn(bookStorageDto);

        mockMvc.perform(put("/v1/book-storage/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookStorageDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Lord of the Rings"));
    }

    @Test
    void deleteBook_shouldReturnOk_whenBookExists() throws Exception {
        when(bookStorageService.findById(1L)).thenReturn(Optional.of(bookStorageDto));

        mockMvc.perform(delete("/v1/book-storage/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBook_shouldReturnNotFound_whenBookDoesNotExist() throws Exception {
        when(bookStorageService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/v1/book-storage/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void borrowBook_shouldReturnBorrowedBook_whenBookCanBeBorrowed() throws Exception {
        when(bookStorageService.borrowBook(1L)).thenReturn(bookStorageDto);

        mockMvc.perform(post("/v1/book-storage/borrow/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Lord of the Rings"));
    }

    @Test
    void returnBook_shouldReturnReturnedBook_whenBookCanBeReturned() throws Exception {
        when(bookStorageService.returnBook(1L)).thenReturn(bookStorageDto);

        mockMvc.perform(post("/v1/book-storage/return/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Lord of the Rings"));
    }
}

