package com.example.demo.serviceImpl;

import com.example.demo.dto.MemberSummaryDTO;
import com.example.demo.exceptions.MemberNotFoundException;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberSummaryDTO getMemberSummary(Long memberId) {
        return memberRepository.findMemberSummaryById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));
    }
}
