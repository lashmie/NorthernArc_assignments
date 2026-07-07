package Payement;

public class Credit implements Payment{
    private double amount;
    Credit(double amount){
        this.amount=amount;
    }
    public double getAmount(){
        return amount;
    }

    public void paymentCheck(){
        System.out.println("The amount "+this.amount+"is payed via CRedit card");
    }
}
