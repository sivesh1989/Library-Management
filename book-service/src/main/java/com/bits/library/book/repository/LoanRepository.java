package com.bits.library.book.repository;

import com.bits.library.book.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByMemberId(Long memberId);

    long countByMemberIdAndReturnedOnIsNull(Long memberId);

    boolean existsByMemberIdAndBookIdAndReturnedOnIsNull(Long memberId, Long bookId);
}
