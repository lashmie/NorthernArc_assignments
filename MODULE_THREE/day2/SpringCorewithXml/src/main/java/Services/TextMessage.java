package Services;

public class TextMessage implements NotificationService{
    @Override
    public void message(String message) {
        System.out.println(message);
    }
}
