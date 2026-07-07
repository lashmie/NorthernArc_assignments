package demo;

public class Upi implements PaymentService{
    @Override
    public void pay(double d) {
        System.out.println("Paying via upi"+d);

    }
}
