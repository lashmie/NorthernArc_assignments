package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @NotBlank(message = "Member name cannot be blank")
    @Size(min = 3, max = 50, message = "Member name must contain 3 to 50 characters")
    private String memberName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Enter a valid email")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password must contain at least 6 characters")
    private String password;

    @NotBlank(message = "Membership branch cannot be blank")
    private String membershipBranch;


    private String role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<IssueRecord> issueRecords = new ArrayList<>();

    public List<IssueRecord> getIssueRecords() {
        return issueRecords;
    }

}
