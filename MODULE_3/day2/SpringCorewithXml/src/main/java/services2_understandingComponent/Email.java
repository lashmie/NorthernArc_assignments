package services2_understandingComponent;

import Services.NotificationService;

public class Email implements NotificationService {
    @Override
    public void message(String message) {
        System.out.println(message);
    }
}
