package com.example.demo.controller;

import com.example.demo.dto.MemberSummaryDTO;
import com.example.demo.service.MemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/members", "/members"})
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{memberId}/summary")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public MemberSummaryDTO getMemberSummary(@PathVariable Long memberId) {
        return memberService.getMemberSummary(memberId);
    }
}

