public class Expense {

    private String ownerName;
    private double balance;

    // Constructor
    public Expense(String ownerName, double balance) {
        this.ownerName = ownerName;
        this.balance = balance;
    }

    // deposit
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Amount deposited successfully");
    }

    // withdraw
    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient balance");
        } else {
            balance -= amount;
            System.out.println("Withdrawal successful");
        }
    }

    // check balance
    public void checkBalance() {
        System.out.println("Balance: " + balance);
    }

    // print details
    public void printDetails() {
        System.out.println("Owner Name: " + ownerName);
        System.out.println("Balance: " + balance);
    }
}