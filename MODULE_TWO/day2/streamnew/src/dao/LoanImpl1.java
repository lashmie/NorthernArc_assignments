//package dao;
//
//import entity.Loan;
//
//import java.util.List;
//import java.util.function.BinaryOperator;
//
//public class LoanImpl1 implements LoanDao{
//
//    @Override
//    public void save(Loan loan) {
//
//    }
//
//    @Override
//    public List<Loan> filterByRejected(List<Loan> loan) {
//        return List.of();
//    }
//
//    @Override
//    public List<Loan> setLoanInterest(List<Loan> loan) {
//        return List.of();
//    }
//
//    @Override
//    public Loan max(List<Loan> loan) {
//    return loan.stream().max((l1,l2)->l1.getLoanamount()- l2.getLoanamount());
//    }
//
//
//    @Override
//    public Loan min(List<Loan> loan) {
//    return loan
//    }
//
//    @Override
//    public Loan total(List<Loan> loan) {
//
//    }
//}
