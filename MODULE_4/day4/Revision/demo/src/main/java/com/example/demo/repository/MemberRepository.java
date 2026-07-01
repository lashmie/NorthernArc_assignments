package com.example.demo.repository;

import com.example.demo.dto.MemberSummaryDTO;
import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    List<Member> findByMembershipBranch(String branch);

    @Query("""
    SELECT m
    FROM Member m
    JOIN m.issueRecords ir
    GROUP BY m
    HAVING COUNT(ir) > :targetCount
    """)
    List<Member> findAvidReaders(@Param("targetCount") Long targetCount);

    @Query("""
    SELECT m.membershipBranch, SUM(ft.amount)
    FROM Member m
    JOIN m.issueRecords ir
    JOIN ir.book b
    JOIN FineTransaction ft ON ft.book = b
    GROUP BY m.membershipBranch
    """)
    List<Object[]> findTotalFinesPerBranch();

    default List<Object[]> findTotalFinesPaidPerBranch() {
        return findTotalFinesPerBranch();
    }

    @Query("""
    SELECT m
    FROM Member m
    JOIN m.issueRecords ir
    JOIN ir.book b
    GROUP BY m
    HAVING COUNT(DISTINCT b.bookType) > 1
    """)
    List<Member> findMembersHoldingMultiGenreBookTypes();

    default List<Member> findMembersHoldingMultiGenreBooks() {
        return findMembersHoldingMultiGenreBookTypes();
    }

    @Query("""
    SELECT new com.example.demo.dto.MemberSummaryDTO(
        m.memberName,
        m.membershipBranch,
        COUNT(DISTINCT ir.issueId),
        COALESCE(SUM(ft.amount), 0)
    )
    FROM Member m
    LEFT JOIN m.issueRecords ir
    LEFT JOIN ir.book b
    LEFT JOIN b.fineTransactions ft
    WHERE m.memberId = :memberId
    GROUP BY m.memberId, m.memberName, m.membershipBranch
    """)
    Optional<MemberSummaryDTO> findMemberSummaryById(@Param("memberId") Long memberId);

    @Query("""
    SELECT m.membershipBranch
    FROM Member m
    LEFT JOIN m.issueRecords ir
    LEFT JOIN ir.book b
    LEFT JOIN b.fineTransactions ft
    GROUP BY m.membershipBranch
    ORDER BY COALESCE(SUM(ft.amount), 0) DESC, m.membershipBranch ASC
    """)
    List<String> findTopBranchByTotalFineCollection(org.springframework.data.domain.Pageable pageable);

    @Query("""
    SELECT m.memberName
    FROM Member m
    LEFT JOIN m.issueRecords ir
    LEFT JOIN ir.book b
    LEFT JOIN b.fineTransactions ft
    GROUP BY m.memberId, m.memberName
    ORDER BY COALESCE(SUM(ft.amount), 0) DESC, m.memberName ASC
    """)
    List<String> findHighestFinePayingMemberName(org.springframework.data.domain.Pageable pageable);



}
