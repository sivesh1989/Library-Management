package com.bits.library.book.service;

import com.bits.library.book.dto.BookRequest;
import com.bits.library.book.dto.BookResponse;
import com.bits.library.book.exception.ConflictException;
import com.bits.library.book.exception.NotFoundException;
import com.bits.library.book.model.Book;
import com.bits.library.book.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository books;

    public BookService(BookRepository books) {
        this.books = books;
    }

    public BookResponse addBook(BookRequest req) {
        if (books.existsByIsbn(req.isbn())) {
            throw new ConflictException("A book with ISBN " + req.isbn() + " already exists");
        }
        Book saved = books.save(new Book(req.isbn(), req.title(), req.author(), req.copies()));
        log.info("Book added: id={} isbn={}", saved.getId(), saved.getIsbn());
        return BookResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public BookResponse findBook(Long id) {
        return BookResponse.of(books.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id " + id)));
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAllBooks() {
        return books.findAll().stream().map(BookResponse::of).toList();
    }
}
