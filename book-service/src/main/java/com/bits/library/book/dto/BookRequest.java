package com.bits.library.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank(message = "isbn is required")
        @Size(max = 20, message = "isbn must be at most 20 characters")
        String isbn,

        @NotBlank(message = "title is required")
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @NotBlank(message = "author is required")
        @Size(max = 120, message = "author must be at most 120 characters")
        String author,

        @NotNull(message = "copies is required")
        @Min(value = 1, message = "copies must be at least 1")
        Integer copies) {
}
