package demo;

public class BigFactory1 {
    private static CreditCard c1 = new CreditCard();
    private static DebitCard d1 = new DebitCard();
    private static Upi u1 = new Upi();
    private static EmailNotification email = new EmailNotification();
    private static WhatsAppNotification wa = new WhatsAppNotification();
    public static PaymentService getPaymentService(String paymentType){

        if(paymentType.equalsIgnoreCase("credit")){

            return c1;

        }else if(paymentType.equalsIgnoreCase("debit")){

            return d1;

        }else if(paymentType.equalsIgnoreCase("upi")){

            return u1;

        }

        return null;

    }

    public static NotificatonService getNotificationService(String notificationType){

        if(notificationType.equalsIgnoreCase("email")){

            return email;

        }else if(notificationType.equalsIgnoreCase("whatsapp")){

            return wa;

        }

        return null;
    }
}
