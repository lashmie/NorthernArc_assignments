package entity;

public class Loan {
    private int loanId;
    private String customerName;
    private double loanAmount;
    private double interestRate;
    private int tenureMonths;

    public Loan(int loanId, String customerName,
                double loanAmount, double interestRate,
                int tenureMonths) {
        this.loanId = loanId;
        this.customerName = customerName;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getTenureMonths(){
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    @Override
    public String toString() {

        return "Loan{" +
                "loanId=" + loanId +
                ", customerName='" + customerName + '\'' +
                ", loanAmount=" + loanAmount +
                ", interestRate=" + interestRate +
                ", tenureMonths=" + tenureMonths +
                '}';
    }

}