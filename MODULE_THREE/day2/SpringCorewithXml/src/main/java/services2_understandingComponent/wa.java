package services2_understandingComponent;

import Services.NotificationService;

public class wa implements NotificationService {
    @Override
    public void message(String message) {
        System.out.println(message);
    }
}
