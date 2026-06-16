package demo;

public class BigFactory {

    private static CreditCard creditCard=new CreditCard();

    private static DebitCard debitCard=new DebitCard();

    private static Upi upi=new Upi();

    private static EmailNotification emailNotification=new EmailNotification();

    private static WhatsAppNotification whatsAppNotification=new WhatsAppNotification();

    public static PaymentService getPaymentService(String paymentType){

        if(paymentType.equalsIgnoreCase("credit")){

            return creditCard;

        }else if(paymentType.equalsIgnoreCase("debit")){

            return debitCard;

        }else if(paymentType.equalsIgnoreCase("upi")){

            return upi;

        }

        return null;

    }

    public static NotificatonService getNotificationService(String notificationType){

        if(notificationType.equalsIgnoreCase("email")){

            return emailNotification;

        }else if(notificationType.equalsIgnoreCase("whatsapp")){

            return whatsAppNotification;

        }

        return null;

    }

}



