package services2_understandingComponent;

import Services.NotificationService;
import Services.PaymentService;

public class ExpenseManager {
    PaymentService paymentService;
    NotificationService notificationService;

    ExpenseManager(PaymentService paymentService, NotificationService notificationService){
        this.paymentService=paymentService;
        this.notificationService=notificationService;
    }

    public void gasbill(double amount){
        paymentService.pay(amount);
        notificationService.message("Gas bill is paying..........");
    }
    public void ebBill(double amount){
        paymentService.pay(amount);
        notificationService.message("eb bill is paying..............");
    }
    public void netflixBill(double amount){
        paymentService.pay(amount);
        notificationService.message("netflixBill is paying..............."+amount);
    }
}
