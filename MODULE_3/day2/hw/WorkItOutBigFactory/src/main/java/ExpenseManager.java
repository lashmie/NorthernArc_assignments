public class ExpenseManager {
    NotificationService notificationService;
    PaymentService paymentService;

    public ExpenseManager(NotificationService notificationService,PaymentService paymentService) {
        this.notificationService = notificationService;
        this.paymentService=paymentService;
    }
    public void payEbBill(int amount){
        notificationService.message("Paying eb bill");
        paymentService.pay(amount);
    }
    public void payWaterBill(int amount){
        notificationService.message("Paying water bill");
        paymentService.pay( amount);
    }
    public void payNetflixBill(int amount){
        notificationService.message("Paying netflix bill");
        paymentService.pay(amount);
    }
}
