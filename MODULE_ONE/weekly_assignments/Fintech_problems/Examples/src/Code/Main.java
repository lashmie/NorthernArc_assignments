package Code;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {

        //loan officer
        LoanOfficer l1 = new LoanOfficer("lavanya");
        //association property is added here

        Scanner sc = new Scanner(System.in);
        System.out.println("Choose the loan type 1.carloan 2.homeloan 3.bikeloan ");
        int val= sc.nextInt();
        System.out.println();
        System.out.println("Enter the customer name: ");
        String name= sc.next();
        System.out.println();
        System.out.println("Enter the loan amount");
        double amt =sc.nextInt();
        System.out.println();
        Loan loan;
        switch (val){
            case 1:
                System.out.println("Car loan");
                loan = new Carloan(name,amt);
                l1.reviewLoan(loan);
                loan.showEMI();
                System.out.println(loan.calculateInterest());
                break;

            case 2:
                System.out.println("homee loan");
                loan = new Homeloan(name,amt);
                l1.reviewLoan(loan);
                loan.showEMI();
                System.out.println(loan.calculateInterest());
                break;
            case 3:
                System.out.println("bike loan");
                loan= new Bikeloan(name,amt);
                l1.reviewLoan(loan);
                loan.showEMI();
                System.out.println(loan.calculateInterest());
                break;
            default:
                System.out.println("Invalid option");
                return;

        }

    }
}
