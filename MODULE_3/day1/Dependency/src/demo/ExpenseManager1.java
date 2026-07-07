package demo;

public class ExpenseManager1 {
    private  NotificatonService ns;
    private PaymentService ps;

    public void setNs(NotificatonService ns) {
        this.ns = ns;
    }

    public void setPs(PaymentService ps) {
        this.ps = ps;
    }

    public void payGasbill(double amount){
        System.out.println("paying gas bill...........");
        ps.pay(amount);
        ns.message();
    }
    public void payWaterbill(double amount){
        System.out.println("paying water bill...........");
        ps.pay(amount);
        ns.message();
    }
    public void payElectricitybill(double amount){
        System.out.println("paying Electricity bill...........");
        ps.pay(amount);
        ns.message();
    }
}
