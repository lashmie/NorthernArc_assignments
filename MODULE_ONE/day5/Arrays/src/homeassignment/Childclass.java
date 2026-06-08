package homeassignment;

public class Childclass {
    private String fname;
    private String lname;
    private String dob;
    Childclass(String fname,String lname,String dob){
        this.fname=fname;
        this.lname=lname;
        this.dob=dob;

    }
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String toString(){
        return fname+" "+lname+" dob";
    }
}
