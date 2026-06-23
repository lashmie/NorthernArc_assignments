package services2_understandingComponent;

import Services.PaymentService;

public class DebitCard implements PaymentService {
    @Override
    public void pay(double amount) {
        System.out.println("Paying :"+amount);
    }
}
