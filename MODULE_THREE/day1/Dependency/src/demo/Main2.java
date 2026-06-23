package demo;

import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter payment type: credit/debit/upi");
        String paymentType = scanner.next();

        System.out.println("Enter notification type: email/whatsapp");
        String notificationType = scanner.next();
        PaymentService paymentService =
                BigFactory.getPaymentService(paymentType);

        NotificatonService notificationService =
                BigFactory.getNotificationService(notificationType);

        ExpenseManager expenseManager = new ExpenseManager(paymentService,notificationService);
        expenseManager.payElectricityBill(1000);
        expenseManager.payWaterBill(200);
        expenseManager.payGasBill(100);
    }
}
