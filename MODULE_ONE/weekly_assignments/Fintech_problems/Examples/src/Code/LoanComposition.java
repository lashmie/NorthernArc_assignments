package Code;

abstract class LoanComposition{

    private String customerName;
    private double loanAmount;

    // COMPOSITION
    private EMISchedule emiSchedule;

    LoanComposition(String customerName, double loanAmount) {
        this.customerName = customerName;
        this.loanAmount = loanAmount;

        // Loan CREATES EMI → strong ownership
        this.emiSchedule = new EMISchedule(60);
    }

    public void showEMI() {
        emiSchedule.generate();
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public abstract double calculateInterest();
}