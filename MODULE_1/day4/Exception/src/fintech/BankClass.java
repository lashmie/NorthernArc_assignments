package fintech;

public class BankClass {
    double init;
    double amount;
    BankClass(double init){
        this.init=init;

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void withdraw(double wamount)throws BankException{
        if(wamount>amount){
            throw new BankException("Amount is not sufficient");
        }

        amount=amount-wamount;
        System.out.println("The transaction is processing");
        System.out.println("The current balance is "+amount);

    }
}
