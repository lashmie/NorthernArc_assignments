package ui;

import dao.LoanDao;
import dao.LoanDaoImpl;
import entity.Loan;

public class LoanApp {

    public static void main(String[] args) {

        LoanDao dao = new LoanDaoImpl();

        Loan loan1 =
                new Loan(101,"Rahul",500000,11.5,60);

        Loan loan2 =
                new Loan(102,"Priya",300000,10.5,48);

        dao.save(loan1);
        dao.save(loan2);

        System.out.println("\n===== ALL LOANS =====");
        dao.findAll().forEach(System.out::println);

        Loan updatedLoan =
                new Loan(101,"Rahul",600000,11.0,72);

        dao.update(updatedLoan);

        System.out.println("\n===== AFTER UPDATE =====");
        dao.findAll().forEach(System.out::println);

        dao.deleteById(102);

        System.out.println("\n===== AFTER DELETE =====");
        dao.findAll().forEach(System.out::println);
    }
}
