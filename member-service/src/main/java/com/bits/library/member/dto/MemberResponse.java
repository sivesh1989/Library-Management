package com.bits.library.member.dto;

import com.bits.library.member.model.Member;

import java.time.LocalDate;

public record MemberResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String membershipType,
        LocalDate joinedOn) {

    public static MemberResponse of(Member m) {
        return new MemberResponse(m.getId(), m.getFullName(), m.getEmail(), m.getPhone(),
                m.getMembershipType().name(), m.getJoinedOn());
    }
}
