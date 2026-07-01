package com.example.demo.service;

import com.example.demo.dto.MemberSummaryDTO;

public interface MemberService {
	MemberSummaryDTO getMemberSummary(Long memberId);
}

