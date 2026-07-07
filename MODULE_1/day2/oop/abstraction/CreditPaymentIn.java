class CreditPaymentIn implements PaymentInterface{
public void payment(Double money){
    System.out.println(money+" is sent via credit card" );
}
}