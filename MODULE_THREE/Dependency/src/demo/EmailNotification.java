package demo;

public class EmailNotification implements NotificatonService {
    @Override
    public void message() {
        System.out.println("Email message sent .........");
    }
}
