public class RechargeTracker {

    private double balance;
    private double chargePerMin;

    // Constructor
    public RechargeTracker(double balance, double chargePerMin) {
        this.balance = balance;
        this.chargePerMin = chargePerMin;
    }

    // topup
    public void topup(double amount) {
        balance += amount;
        System.out.println("Topup successful");
    }

    // make call
    public void makecall(double min) {
        double cost = min * chargePerMin;

        if (cost > balance) {
            System.out.println("Insufficient balance for call");
        } else {
            balance -= cost;
            System.out.println("Call completed for " + min + " minutes");
        }
    }

    // check balance
    public void checkBalance() {
        System.out.println("Balance: " + balance);
    }
}