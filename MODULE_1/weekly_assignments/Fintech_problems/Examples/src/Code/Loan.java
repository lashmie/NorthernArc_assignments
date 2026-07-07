package Code;
//Encapsulation
abstract class Loan {
    private String customerName;
    private double loanamount;
    // COMPOSITION
    private EMISchedule emiSchedule;

    Loan(String customerName, double loanAmount) {
        this.customerName = customerName;
        this.loanamount = loanAmount;

        this.emiSchedule = new EMISchedule(60);
    }

    public void showEMI() {
        emiSchedule.generate();
    }

//    Loan(String customerName,double loanamount){
//        this.customerName=customerName;
//        this.loanamount=loanamount;
//    }
    public void setCustomerName(String customerName){
        this.customerName=customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getLoanAmount() {
        return loanamount;
    }
    public void setLoanamount(double amount){
        if(amount>0){
            this.loanamount=amount;
        }
    }
    //astract method to show abstraction:
    public abstract double calculateInterest();


    }


