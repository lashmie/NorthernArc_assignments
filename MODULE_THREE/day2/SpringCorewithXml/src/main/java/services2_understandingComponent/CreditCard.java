package services2_understandingComponent;

import Services.PaymentService;

public class CreditCard implements PaymentService {
    @Override
    public void pay(double amount) {
        System.out.println("Paying via"+amount);
    }
}
