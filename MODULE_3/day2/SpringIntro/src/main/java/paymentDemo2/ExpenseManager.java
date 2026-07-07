package paymentDemo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class ExpenseManager {
    @Autowired
            @Qualifier("debit")
    PaymentService paymentService;
    @Autowired
            @Qualifier("wa")
    NotificationService notificationService;

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
