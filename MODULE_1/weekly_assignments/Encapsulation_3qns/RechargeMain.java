import java.util.Scanner;

public class RechargeMain {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter initial balance:");
        double bal = sc.nextDouble();

        System.out.println("Enter charge per minute:");
        double rate = sc.nextDouble();

        RechargeTracker rt = new RechargeTracker(bal, rate);

        while (true) {
            System.out.println("\n1.Topup 2.Call 3.Check 4.Exit");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("Enter amount:");
                    rt.topup(sc.nextDouble());
                    break;

                case 2:
                    System.out.println("Enter minutes:");
                    rt.makecall(sc.nextDouble());
                    break;

                case 3:
                    rt.checkBalance();
                    break;

                case 4:
                    return;
            }
        }
    }
}