package paymentDemo2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("textMessage")
public class TextMessage implements NotificationService{
    @Override
    public void message(String message) {
        System.out.println(message);
    }
}
