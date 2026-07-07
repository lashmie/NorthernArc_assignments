package demo;
public class WhatsAppNotification implements NotificatonService {
    @Override
    public void message() {
        System.out.println("Wa message sent:..");
    }
}
