class Debit extends Payment{
     protected double amount;
    public void send(double amount){
        System.out.println("You are making debit card payment of Rs."+amount);
    }
}