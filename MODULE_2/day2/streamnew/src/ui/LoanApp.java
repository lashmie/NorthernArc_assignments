//package ui;//package ui;
////
////import dao.LoanDao;
////import dao.LoanImpl;
////import entity.Loan;
////
////import java.util.ArrayList;
////import java.util.Comparator;
////import java.util.List;
////
////public class LoanApp {
////
////    public static void main(String[] args) {
////        List<Loan> loan = new ArrayList<>();
////        loan.add(new Loan(1, 10000, 12, 10, "approved", "vehicle loan"));
////        loan.add(new Loan(2, 100000, 11, 18, "approved", "vehicle loan"));
////
////        List<Loan> rejectedLoans = loan.stream().filter((loan1) -> loan1.getLoanstatus().equals("rejected")).toList();
////
////        List<Loan> updateLoan = loan.stream()
////                .filter((loan2) -> {
////                    return loan2.getLoantype().equalsIgnoreCase(" vehicle loan");
////
////                })
////                .sorted((l1, l2) -> {
////                    return l1.getLoanamount() - l2.getLoanamount();
////                })
////                .sorted(Comparator.comparing(Loan::getLoanamount)
////                        .reversed()
////                        .thenComparing(Loan::getLoaninterest)
////                )
////                .map((loan1) -> {
////                    loan1.setLoaninterest(loan1.getLoaninterest() + 2);
////                    return loan1;
////
////                }).toList();
////
////        //
////        int val =loan.stream().max((Loan l1,Loan l2))->l1.getLoanAmount()-l2.getLoanamount()));
////
////
////    }
////}
//
//import dao.LoanDao;
//import dao.LoanImpl;
//import entity.Loan;
//
//import java.util.List;
//
//public class LoanApp {
//
//    public static void main(String[] args) {
//
////        LoanDao dao = new LoanImpl();
//
//        List<Loan> loans = List.of(
//                new Loan(1, 10000, 12, 10, "approved", "vehicle loan")
//
//        );
//
////        System.out.println("Rejected Loans");
////
////        List<Loan> rejected =
////                dao.filterByRejected(loans);
////
////        rejected.forEach(System.out::println);
////
////        System.out.println("\nInterest Updated");
////
////        List<Loan> updated =
////                dao.setLoanInterest(loans);
////
////        updated.forEach(System.out::println);
////max loan
//        System.out.println(loans.stream().max((l1,l2)->l1.getLoanamount()- l2.getLoanamount()));
////min loan
//        System.out.println(loans.stream().min((l1,l2)->l1.getLoanamount()-l2.getLoanamount()));
////reduce function
//        int total=loans.stream().mapToInt(Loan::getLoanamount).reduce(0,(a,cur)->a+cur);
//        loans.stream().forEach((loan)-> System.out.println(loan));
//        System.out.println("--------------------");
//        loans.stream().forEach(System.out::println);
//    }
//}
