package com.bits.library.book.dto;

import com.bits.library.book.model.Book;

public record BookResponse(
        Long id,
        String isbn,
        String title,
        String author,
        int totalCopies,
        int availableCopies) {

    public static BookResponse of(Book b) {
        return new BookResponse(b.getId(), b.getIsbn(), b.getTitle(), b.getAuthor(),
                b.getTotalCopies(), b.getAvailableCopies());
    }
}
