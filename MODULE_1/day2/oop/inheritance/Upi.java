class Upi extends Payment{
     protected double amount;
    public void send(double amount){
        System.out.println("You are making upi payment of Rs."+amount);
    }
}