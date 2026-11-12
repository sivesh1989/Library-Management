package com.bits.library.member.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MembershipType membershipType;

    @Column(nullable = false, updatable = false)
    private LocalDate joinedOn;

    protected Member() {
    }

    public Member(String fullName, String email, String phone, MembershipType membershipType) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
    }

    @PrePersist
    void setJoinDate() {
        this.joinedOn = LocalDate.now();
    }

    public void applyChanges(String fullName, String email, String phone, MembershipType membershipType) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public LocalDate getJoinedOn() {
        return joinedOn;
    }
}
