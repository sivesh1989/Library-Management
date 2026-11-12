package com.bits.library.member.dto;

import com.bits.library.member.model.MembershipType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberRequest(
        @NotBlank(message = "fullName is required")
        @Size(max = 100, message = "fullName must be at most 100 characters")
        String fullName,

        @NotBlank(message = "email is required")
        @Email(message = "email is not valid")
        @Size(max = 150, message = "email must be at most 150 characters")
        String email,

        @Pattern(regexp = "^$|^[0-9+\\- ]{7,15}$", message = "phone must have 7 to 15 digits")
        String phone,

        @NotNull(message = "membershipType is required (STANDARD or PREMIUM)")
        MembershipType membershipType) {
}
