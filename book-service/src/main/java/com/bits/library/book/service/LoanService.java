package com.bits.library.book.service;

import com.bits.library.book.client.MemberClient;
import com.bits.library.book.config.LibraryRules;
import com.bits.library.book.dto.BorrowRequest;
import com.bits.library.book.dto.LoanResponse;
import com.bits.library.book.dto.MemberDto;
import com.bits.library.book.exception.ConflictException;
import com.bits.library.book.exception.NotFoundException;
import com.bits.library.book.model.Book;
import com.bits.library.book.model.Loan;
import com.bits.library.book.repository.BookRepository;
import com.bits.library.book.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LoanService {

    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

    private final LoanRepository loans;
    private final BookRepository books;
    private final MemberClient memberClient;
    private final LibraryRules rules;

    public LoanService(LoanRepository loans, BookRepository books, MemberClient memberClient, LibraryRules rules) {
        this.loans = loans;
        this.books = books;
        this.memberClient = memberClient;
        this.rules = rules;
    }

    // ---------- commands ----------

    /** Borrow a book: checks book, member (REST call), copy availability and the member's loan limit. */
    public LoanResponse borrow(BorrowRequest req) {
        Book book = findBookOrThrow(req.bookId());

        MemberDto member = memberClient.fetchMember(req.memberId());

        if (loans.existsByMemberIdAndBookIdAndReturnedOnIsNull(member.id(), book.getId())) {
            throw new ConflictException("Member " + member.id() + " has already borrowed '" + book.getTitle()
                    + "' and not returned it");
        }
        if (!book.hasFreeCopy()) {
            throw new ConflictException("No copies of '" + book.getTitle() + "' are available right now");
        }
        long activeLoans = loans.countByMemberIdAndReturnedOnIsNull(member.id());
        int limit = rules.maxLoansFor(member.membershipType());
        if (activeLoans >= limit) {
            throw new ConflictException("Loan limit reached: " + member.membershipType() + " members can hold "
                    + limit + " books at a time");
        }

        book.checkOut();
        Loan loan = loans.save(new Loan(member.id(), member.fullName(), book.getId(), rules.loanDays()));
        log.info("Member {} borrowed book {} (loan {})", member.id(), book.getId(), loan.getId());
        return LoanResponse.of(loan, book);
    }

    /** Return a book: closes the loan and puts the copy back. */
    public LoanResponse giveBack(Long loanId) {
        Loan loan = loans.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with id " + loanId));
        if (!loan.isActive()) {
            throw new ConflictException("Loan " + loanId + " was already returned");
        }
        Book book = findBookOrThrow(loan.getBookId());
        loan.markReturned();
        book.checkIn();
        log.info("Loan {} returned", loanId);
        return LoanResponse.of(loan, book);
    }

    // ---------- queries ----------

    @Transactional(readOnly = true)
    public LoanResponse findLoan(Long loanId) {
        Loan loan = loans.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with id " + loanId));
        return toResponse(loan);
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findAllLoans() {
        return loans.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findLoansOfMember(Long memberId) {
        return loans.findByMemberId(memberId).stream().map(this::toResponse).toList();
    }

    private LoanResponse toResponse(Loan loan) {
        return LoanResponse.of(loan, findBookOrThrow(loan.getBookId()));
    }

    private Book findBookOrThrow(Long bookId) {
        return books.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found with id " + bookId));
    }
}
