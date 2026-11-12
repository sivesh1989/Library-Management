package com.bits.library.book.client;

import com.bits.library.book.dto.MemberDto;
import com.bits.library.book.exception.MemberServiceUnavailableException;
import com.bits.library.book.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/** Calls the Member Service over REST. */
@Component
public class MemberClient {

    private static final Logger log = LoggerFactory.getLogger(MemberClient.class);

    private final RestClient restClient;

    public MemberClient(RestClient memberRestClient) {
        this.restClient = memberRestClient;
    }

    public MemberDto fetchMember(Long memberId) {
        log.info("Calling Member Service: GET /api/members/{}", memberId);
        try {
            MemberDto member = restClient.get()
                    .uri("/api/members/{id}", memberId)
                    .retrieve()
                    .onStatus(status -> status.value() == 404, (request, response) -> {
                        throw new NotFoundException("Member not found with id " + memberId);
                    })
                    .body(MemberDto.class);
            if (member == null) {
                throw new NotFoundException("Member not found with id " + memberId);
            }
            return member;
        } catch (RestClientException ex) {
            log.error("Member Service call failed: {}", ex.getMessage());
            throw new MemberServiceUnavailableException("Member Service is not reachable, please try again later");
        }
    }
}
