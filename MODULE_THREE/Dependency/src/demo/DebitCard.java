package demo;

public class DebitCard implements PaymentService{
    @Override
    public void pay(double d) {
        System.out.println("Payment is done via debit card ...........");
    }

    public static class EmailNotification {
    }
}
