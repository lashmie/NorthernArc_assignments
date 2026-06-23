import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        PaymentService paymentService = new CreditCard();
//        NotificationService notificationService = new Email();
//        ExpenseManager expenseManager = new ExpenseManager(notificationService,paymentService);
//        expenseManager.payEbBill(1000);
//        expenseManager.payWaterBill(980);
//        expenseManager.payNetflixBill(1200);
        BigFactory factory = new BigFactory();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the payment type");
        String paymentService=sc.next();
        System.out.println("Enter the notification type");
        String notificationService=sc.next();
        ExpenseManager expenseManager =factory.whole(paymentService,notificationService);
        expenseManager.payEbBill(100);
    }
}
