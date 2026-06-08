package Payement;

public class Debit implements Payment{
    private double amount;
    Debit(double amount){
        this.amount=amount;
    }
    public double getAmount(){
        return amount;
    }

    public void paymentCheck(){
        System.out.println("The amount "+this.amount+"is payed via Debit");
    }
}
