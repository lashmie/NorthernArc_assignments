package Code;
//Loan has a Loan Officer -> has a relationship
//loan many or may not have loan officer
public class LoanOfficer   {
    private String name;
    LoanOfficer(String name){

        this.name=name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void approveLoan(){
        System.out.println("Officer name : "+ this.getName());
    }

    public void reviewLoan(Loan loan){
        System.out.println("The loan amount"+loan.getLoanAmount()+"for customer"+loan.getCustomerName()+"is processed"+"by loan officer"+this.getName());
    }



}
