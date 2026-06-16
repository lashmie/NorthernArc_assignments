package dao;

import entity.Loan;
import java.util.List;

public interface LoanDao {

    void save(Loan loan);

    List<Loan> findAll();

    void update(Loan loan);

    void deleteById(int loanId);

    void deleteAll();
}