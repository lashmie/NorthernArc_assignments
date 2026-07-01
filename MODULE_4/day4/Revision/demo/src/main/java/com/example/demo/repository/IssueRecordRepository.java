package com.example.demo.repository;

import com.example.demo.model.FineTransaction;
import com.example.demo.model.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRecordRepository extends JpaRepository<IssueRecord,Long> {
}
