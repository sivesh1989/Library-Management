package com.bits.library.book.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * One borrowing of one book by one member. The member lives in the Member Service,
 * so only its id is stored as a plain number. The name is copied for easy listing.
 */
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 100)
    private String memberName;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime borrowedOn;

    @Column(nullable = false, updatable = false)
    private LocalDate dueDate;

    private LocalDateTime returnedOn;

    protected Loan() {
    }

    public Loan(Long memberId, String memberName, Long bookId, int loanDays) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.bookId = bookId;
        this.borrowedOn = LocalDateTime.now();
        this.dueDate = LocalDate.now().plusDays(loanDays);
    }

    public boolean isActive() {
        return returnedOn == null;
    }

    public void markReturned() {
        this.returnedOn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public Long getBookId() {
        return bookId;
    }

    public LocalDateTime getBorrowedOn() {
        return borrowedOn;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDateTime getReturnedOn() {
        return returnedOn;
    }
}
