package homeassignment;

public class Childclass implements Comparable<Childclass> {
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



    @Override
    public int compareTo(Childclass o) {
        return this.fname.compareTo(o.fname);
    }

    @Override
    public String toString() {
        return fname + " " + lname + " " + dob;
    }
}
