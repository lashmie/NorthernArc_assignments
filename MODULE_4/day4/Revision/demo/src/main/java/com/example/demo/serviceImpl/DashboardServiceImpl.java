package com.example.demo.serviceImpl;

import com.example.demo.dto.DashboardDTO;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.FineTransactionRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.DashboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final FineTransactionRepository fineTransactionRepository;

    public DashboardServiceImpl(
            MemberRepository memberRepository,
            BookRepository bookRepository,
            FineTransactionRepository fineTransactionRepository
    ) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.fineTransactionRepository = fineTransactionRepository;
    }

    @Override
    public DashboardDTO getDashboard() {
        Long totalMembers = memberRepository.count();
        Long totalBooks = bookRepository.count();
        Double totalFinesCollected = fineTransactionRepository.findTotalFinesCollected();

        String topBranch = memberRepository
                .findTopBranchByTotalFineCollection(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse("N/A");

        String highestFinePayingMember = memberRepository
                .findHighestFinePayingMemberName(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse("N/A");

        return new DashboardDTO(
                totalMembers,
                totalBooks,
                totalFinesCollected == null ? 0.0 : totalFinesCollected,
                topBranch,
                highestFinePayingMember
        );
    }
}

