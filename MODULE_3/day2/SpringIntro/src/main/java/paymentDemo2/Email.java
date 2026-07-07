package paymentDemo2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
@Component
@Qualifier("email")
public class Email implements NotificationService {
    @Override
    public void message(String message) {
        System.out.println(message);
    }
}
