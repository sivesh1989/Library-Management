package com.bits.library.book.controller;

import com.bits.library.book.dto.BorrowRequest;
import com.bits.library.book.dto.LoanResponse;
import com.bits.library.book.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    /** Borrow a book. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse borrow(@Valid @RequestBody BorrowRequest request) {
        return service.borrow(request);
    }

    /** Return a book. */
    @PutMapping("/{loanId}/return")
    public LoanResponse giveBack(@PathVariable Long loanId) {
        return service.giveBack(loanId);
    }

    @GetMapping
    public List<LoanResponse> all() {
        return service.findAllLoans();
    }

    @GetMapping("/{loanId}")
    public LoanResponse one(@PathVariable Long loanId) {
        return service.findLoan(loanId);
    }

    @GetMapping("/member/{memberId}")
    public List<LoanResponse> byMember(@PathVariable Long memberId) {
        return service.findLoansOfMember(memberId);
    }
}
