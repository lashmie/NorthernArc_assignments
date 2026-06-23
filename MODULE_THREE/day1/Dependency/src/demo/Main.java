//package demo;
//import java.util.Scanner;
//public class Main {
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter 1 for Debit Card \nEnter 2 for Credit Card \n Enter the 3 for upi");
//        int val = sc.nextInt();
//        ExpenseManager expenseManager;
//        switch(val){
//            case 1:
//                expenseManager= new ExpenseManager(new DebitCard());
//                break;
//            case 2:
//                expenseManager= new ExpenseManager(new CreditCard());
//                break;
//            case 3:
//                expenseManager= new ExpenseManager(new Upi());
//                break;
//            default:
//                System.out.println("Invalid");
//                return;
//        }
//        expenseManager.payElectricityBill(3000);
//        expenseManager.payGasBill(700);
//        expenseManager.payWaterBill(350);
//    }
//}
