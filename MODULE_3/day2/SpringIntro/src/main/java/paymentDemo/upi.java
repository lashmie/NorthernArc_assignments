package paymentDemo;

public class upi implements PaymentService{
    @Override
    public void pay(double amount) {
        System.out.println("Paying "+amount);
    }
}
