package Payement;

public class Upi implements Payment{
    private double amount;
    Upi(double amount){
        this.amount=amount;
    }
    public double getAmount(){
        return amount;
    }

    public void paymentCheck(){
        System.out.println("The amount "+this.amount+"is payed via Upi");
    }
}
