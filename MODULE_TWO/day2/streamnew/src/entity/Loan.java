package entity;

public class Loan {
    private int loanid;
    private int loanamount;
    private int loantenure;
    private int loaninterest;
    private String loanstatus;
    private String loantype;

    public Loan(int loanid, int loanamount, int loanmonths, int loanintere, String loanstatus, String loantype) {
        this.loanid = loanid;
        this.loanamount = loanamount;
        this.loantenure = loanmonths;
        this.loaninterest = loanintere;
        this.loanstatus = loanstatus;
        this.loantype = loantype;
    }

    // Getter for loanstatus
    public String getLoanstatus() {
        return loanstatus;
    }

    // Setter for loanstatus
    public void setLoanstatus(String loanstatus) {
        this.loanstatus = loanstatus;
    }

    // Getter for loantype
    public String getLoantype() {
        return loantype;
    }

    public int getLoaninterest() {
        return loaninterest;
    }

    public void setLoanamount(int loanamount) {
        this.loanamount = loanamount;
    }

    public void setLoaninterest(int loaninterest) {
        this.loaninterest = loaninterest;
    }

    public int getLoanamount() {
        return loanamount;
    }

    // Setter for loantype
    public void setLoantype(String loantype) {
        this.loantype = loantype;
    }
    public String toString() {
        return "The loan id" + this.loanid + "loan amount" + this.loanamount + "Tenure" + this.loantenure;
    }
}
