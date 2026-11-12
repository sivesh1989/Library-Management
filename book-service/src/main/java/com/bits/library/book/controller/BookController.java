package com.bits.library.book.controller;

import com.bits.library.book.dto.BookRequest;
import com.bits.library.book.dto.BookResponse;
import com.bits.library.book.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookRequest request) {
        return service.addBook(request);
    }

    @GetMapping
    public List<BookResponse> list() {
        return service.findAllBooks();
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return service.findBook(id);
    }
}
