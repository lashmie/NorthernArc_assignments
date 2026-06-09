package Code;

public class Homeloan extends Loan{//inheritance is used

    Homeloan(String customerName, double loanamount) {
        super(customerName, loanamount);
    }
//lets take home loan interest is 8 percent
    @Override
    public double calculateInterest() {//abstraction
        return getLoanAmount()*0.08;//encapsulation
    }
}
