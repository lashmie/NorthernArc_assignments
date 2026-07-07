class Credit extends Payment{
     protected double amount;
    public void send(double amount){
        System.out.println("You are making credit card payment of Rs."+amount);
    }
}