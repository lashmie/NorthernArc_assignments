package demo;
public class ExpenseManager {
    private PaymentService paymentService;
    private NotificatonService notificatonService;

    ExpenseManager(PaymentService paymentService,NotificatonService notificatonService) {
        this.paymentService = paymentService;
        this.notificatonService=notificatonService;
    }

    public void payElectricityBill(double amount) {
        System.out.println("Paying the electricity bill :" + amount);
        paymentService.pay(amount);
        notificatonService.message();
        System.out.println("Electricity bill paid");
    }

    public void payWaterBill(double amount) {
        System.out.println("Paying water bill of " + amount);
        paymentService.pay(amount);
        notificatonService.message();
        System.out.println("Water bill paid");
    }

    public void payGasBill(double amount) {
        System.out.println("Paying gas bill" + amount);
        paymentService.pay(amount);
        notificatonService.message();
        System.out.println("gas bill is done");
    }

}