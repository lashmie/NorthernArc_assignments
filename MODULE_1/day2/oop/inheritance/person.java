public class person {
    private String fname;
    private String lname;
    protected int age;

    public void setFname(String name){
        this.fname=name;
    }
    public void setlname(String name){
        this.lname=name;
    }
    public void setAge(int age){
        this.age=age;
    }

    //getters
    public String getFname(){
        return fname;
    }
    public String getLname(){
        return lname;
    }

    public int getAge(){
        return age;
    }
    public void showDetails(){
        System.out.println("fname"+fname);
        System.out.println( "lname"+lname);
        System.out.println("Age"+age);
    }
    
}