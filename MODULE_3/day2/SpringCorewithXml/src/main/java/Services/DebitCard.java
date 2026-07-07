package Services;

public class DebitCard implements PaymentService{
    @Override
    public void pay(double amount) {
        System.out.println("Paying :"+amount);
    }
}
