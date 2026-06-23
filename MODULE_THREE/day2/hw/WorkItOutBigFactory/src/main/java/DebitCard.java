public class DebitCard implements PaymentService{
    @Override
    public void pay(double amt) {
        System.out.println("Paying amount Rs"+amt+"via debit cart");
    }
}
