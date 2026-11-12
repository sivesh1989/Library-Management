package com.bits.library.member.service;

import com.bits.library.member.dto.MemberRequest;
import com.bits.library.member.dto.MemberResponse;
import com.bits.library.member.exception.ConflictException;
import com.bits.library.member.exception.NotFoundException;
import com.bits.library.member.model.Member;
import com.bits.library.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository members;

    public MemberService(MemberRepository members) {
        this.members = members;
    }

    // ---------- commands ----------

    public MemberResponse register(MemberRequest req) {
        if (members.existsByEmailIgnoreCase(req.email())) {
            throw new ConflictException("Email " + req.email() + " is already registered");
        }
        Member saved = members.save(new Member(req.fullName(), req.email(), req.phone(), req.membershipType()));
        log.info("Member created: id={}", saved.getId());
        return MemberResponse.of(saved);
    }

    public MemberResponse update(Long id, MemberRequest req) {
        Member member = load(id);
        members.findByEmailIgnoreCase(req.email())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new ConflictException("Email " + req.email() + " belongs to another member");
                });
        member.applyChanges(req.fullName(), req.email(), req.phone(), req.membershipType());
        log.info("Member updated: id={}", id);
        return MemberResponse.of(member);
    }

    public void remove(Long id) {
        members.delete(load(id));
        log.info("Member deleted: id={}", id);
    }

    // ---------- queries ----------

    @Transactional(readOnly = true)
    public MemberResponse find(Long id) {
        return MemberResponse.of(load(id));
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return members.findAll().stream().map(MemberResponse::of).toList();
    }

    private Member load(Long id) {
        return members.findById(id).orElseThrow(() -> new NotFoundException("Member not found with id " + id));
    }
}
