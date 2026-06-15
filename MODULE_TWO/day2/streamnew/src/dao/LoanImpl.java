//package dao;
//
//import entity.Loan;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.UnaryOperator;
//
//public class LoanImpl implements LoanDao {
//
//    private List<Loan> database = new ArrayList<>();
//
//    @Override
//    public void save(Loan loan) {
//        database.add(loan);
//    }
//
//    @Override
//    public List<Loan> filterByRejected(List<Loan> loans) {
//
//        return loans.stream()
//                .filter(loan ->
//                        loan.getLoanstatus()
//                                .equalsIgnoreCase("rejected"))
//                .toList();
//    }
//
////    class MyOperator implements UnaryOperator<Loan> {
////
////        @Override
////        public Loan apply(Loan loan) {
////            loan.setLoaninterest(
////                    loan.getLoaninterest() + 2
////            );
////            return loan;
////        }
////    }
////
////    @Override
////    public List<Loan> setLoanInterest(List<Loan> loans) {
////
////        MyOperator operator = new MyOperator();
////
////        return loans.stream()
////                .map(operator)
////                .toList();
////    }
//    public List<Loan> setLoanInterest(List<Loan> loans){
//        return loans.stream().map((loan)->{
//            loan.setLoaninterest(loan.getLoaninterest()+2);
//
//        })
//    }
//
//
//}