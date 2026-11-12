package com.bits.library.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** The part of the Member Service response that the Book Service needs. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MemberDto(Long id, String fullName, String email, String membershipType) {
}
