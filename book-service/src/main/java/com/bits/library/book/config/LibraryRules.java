package com.bits.library.book.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Loan rules read from application.properties. */
@Component
public class LibraryRules {

    private final int loanDays;
    private final int maxLoansStandard;
    private final int maxLoansPremium;

    public LibraryRules(@Value("${library.loan-days}") int loanDays,
                        @Value("${library.max-loans.standard}") int maxLoansStandard,
                        @Value("${library.max-loans.premium}") int maxLoansPremium) {
        this.loanDays = loanDays;
        this.maxLoansStandard = maxLoansStandard;
        this.maxLoansPremium = maxLoansPremium;
    }

    public int loanDays() {
        return loanDays;
    }

    public int maxLoansFor(String membershipType) {
        return "PREMIUM".equalsIgnoreCase(membershipType) ? maxLoansPremium : maxLoansStandard;
    }
}
