package dao;

import entity.Loan;

import java.util.List;

public interface LoanDao {
    void save(Loan loan);
    List<Loan> filterByRejected(List<Loan> loan);
    List<Loan> setLoanInterest(List<Loan> loan);
    Loan max(List<Loan> loan);
    Loan min(List<Loan> loan);
    Loan total(List<Loan> loan);


}
