public class BigFactory {
    CreditCard c1 = new CreditCard();
    DebitCard d1= new DebitCard();
    Email email = new Email();
    Wa wa = new Wa();
    public NotificationService getMService(String service){
        if(service.equalsIgnoreCase("email")){
            return email;
        }
        else if(service.equalsIgnoreCase("wa")){
            return wa;
        }
        return null;
    }
    public PaymentService getPayment(String service){
        if(service.equalsIgnoreCase("credit")){
            return c1 ;
        }
        else if(service.equalsIgnoreCase("debit")){
            return d1;
        }
        return null;
    }
    public ExpenseManager whole(String paymenttype,String notification){
        PaymentService paymentService =getPayment(paymenttype);
        NotificationService notificationService=getMService(notification);
        return new ExpenseManager(notificationService,paymentService);
    }
}
