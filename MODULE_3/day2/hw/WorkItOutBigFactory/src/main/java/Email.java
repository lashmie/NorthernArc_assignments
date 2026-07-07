public class Email implements NotificationService{
    @Override
    public void message(String message) {
        System.out.println(message);
    }
}
