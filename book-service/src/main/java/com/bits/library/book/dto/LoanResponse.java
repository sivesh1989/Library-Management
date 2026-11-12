package com.bits.library.book.dto;

import com.bits.library.book.model.Book;
import com.bits.library.book.model.Loan;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanResponse(
        Long loanId,
        Long memberId,
        String memberName,
        Long bookId,
        String isbn,
        String title,
        LocalDateTime borrowedOn,
        LocalDate dueDate,
        LocalDateTime returnedOn,
        String status) {

    public static LoanResponse of(Loan loan, Book book) {
        return new LoanResponse(loan.getId(), loan.getMemberId(), loan.getMemberName(), book.getId(),
                book.getIsbn(), book.getTitle(), loan.getBorrowedOn(), loan.getDueDate(),
                loan.getReturnedOn(), loan.isActive() ? "ACTIVE" : "RETURNED");
    }
}
