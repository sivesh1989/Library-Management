package com.bits.library.book.dto;

import jakarta.validation.constraints.NotNull;

public record BorrowRequest(
        @NotNull(message = "memberId is required")
        Long memberId,

        @NotNull(message = "bookId is required")
        Long bookId) {
}
