import java.util.Scanner;

public class ExpenseMain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter owner name:");
        String name = sc.nextLine();

        System.out.println("Enter initial balance:");
        double bal = sc.nextDouble();

        Expense acc = new Expense(name, bal);

        while (true) {

           
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Exit");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("Enter amount to deposit:");
                    acc.deposit(sc.nextDouble());
                    break;

                case 2:
                    System.out.println("Enter amount to withdraw:");
                    acc.withdraw(sc.nextDouble());
                    break;

                case 3:
                    acc.checkBalance();
                    break;

                case 4:
                    System.out.println("Owner details:");
                    acc.printDetails();
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
}