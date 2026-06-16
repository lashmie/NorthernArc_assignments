package demo;


public class CreditCard implements PaymentService{
    @Override
    public void pay(double d) {
        System.out.println("Payment is done via credit card ...........");
    }


}

