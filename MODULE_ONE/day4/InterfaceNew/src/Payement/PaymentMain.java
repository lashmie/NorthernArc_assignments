package Payement;

public class PaymentMain {
    public static void main(String[] args) {
        Payment p1 = new Upi(100);
        p1.paymentCheck();
        Payment p2 = new Debit(100);
        p2.paymentCheck();
        Payment p3=new Credit(9080);
    p3.paymentCheck();
    }
}
