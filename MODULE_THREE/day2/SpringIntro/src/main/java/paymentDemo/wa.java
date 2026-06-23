package paymentDemo;

public class wa implements NotificationService{
    @Override
    public void message(String message) {
        System.out.println(message);
    }
}
