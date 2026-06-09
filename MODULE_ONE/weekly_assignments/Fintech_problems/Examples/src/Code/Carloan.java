package Code;

public class Carloan extends Loan {
    Carloan(String name,double amt){
        super(name,amt);//constructor chaining=> child constructor calling parent consturcor
    }
    //lets take bike loan is 18%
    @Override
    public double calculateInterest() {
        return getLoanAmount()*0.18;
    }
}
