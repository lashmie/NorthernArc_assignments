package self;

import java.util.Comparator;

public class nbfc implements Comparable<nbfc> {
    private String loanid;
    private String applicant_name;
    private int loanapprovalyear;
    nbfc(String loanid,String applicant_name,int loanapprovalyear){
        this.loanid=loanid;
        this.applicant_name=applicant_name;
        this.loanapprovalyear=loanapprovalyear;
    }

    public void setLoanid(String loanid){
        this.loanid=loanid;
    }
    public String getLoanid(){
        return loanid;
    }
    public String getApplicant_name() {
        return applicant_name;
    }

    // Setter for name
    public void setApplicant_name(String applicant_name) {
        this.applicant_name = applicant_name;
    }

    // Getter for year
    public int getLoanapprovalyear() {
        return loanapprovalyear;
    }

    // Setter for year
    public void setLoanapprovalyear(int loanapprovalyear) {
        this.loanapprovalyear = loanapprovalyear;
    }


    @Override
    public int compareTo(nbfc o) {
        return this.loanid.compareTo(o.loanid);
    }

    @Override
    public String toString() {
        return "values are "+loanid + loanapprovalyear+applicant_name;
    }
}
