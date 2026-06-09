package Code;

public class Bikeloan extends Loan {
    Bikeloan(String name,double amt){
        super(name,amt);//constructor chaining=> child constructor calling parent consturcor
    }
    //lets take bike loan is 12%
    @Override
    public double calculateInterest() {
        return getLoanAmount()*0.12;
    }
}
