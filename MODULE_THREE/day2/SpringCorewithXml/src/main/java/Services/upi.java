package Services;

public class upi implements PaymentService{
    @Override
    public void pay(double amount) {
        System.out.println("Paying "+amount);
    }
}
