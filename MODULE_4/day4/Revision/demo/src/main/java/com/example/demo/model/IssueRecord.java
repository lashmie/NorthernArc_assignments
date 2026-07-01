package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "issue_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    private LocalDate returnDate;

    @NotBlank(message = "Status cannot be blank")
    private String status;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    @NotNull(message = "Member is required")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn")
    @JsonIgnore
    @NotNull(message = "Book is required")
    private Book book;






























    public void setMember(Member member) {
        if (this.member == member) {
            return;
        }
        if (this.member != null) {
            this.member.getIssueRecords().remove(this);
        }
        this.member = member;
        if (member != null && !member.getIssueRecords().contains(this)) {
            member.getIssueRecords().add(this);
        }
    }

    public void setBook(Book book) {
        if (this.book == book) {
            return;
        }
        if (this.book != null) {
            this.book.getIssueRecords().remove(this);
        }
        this.book = book;
        if (book != null && !book.getIssueRecords().contains(this)) {
            book.getIssueRecords().add(this);
        }
    }

}
