package paymentDemo2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@Qualifier("debit")
public class DebitCard implements PaymentService {
    @Override
    public void pay(double amount) {
        System.out.println("Paying :"+amount);
    }
}
