class DebitPaymentIn implements PaymentInterface{
public void payment(Double money){
    System.out.println(money+" is sent via debit card" );
}
}