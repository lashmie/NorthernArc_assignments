package association;

class Person{
    private String fname;
    private String lname;
    private int age;

    public Person(String fname,String lname,int age){
        this.fname=fname;
        this.lname=lname;
        this.age=age;
    }

    //pure encapsulation means using the getter and setter methods because no direct access to the member variables
    //Main3
    public void setFname(String name){
        fname=name;
    }
    public void setLname(String lastname){
        lname=lastname;
    }
    public void setAge(int val){
        age=val;
    }
    public String getFname(){
        return fname;
    }
    public String getLname(){
        return lname;
    }
    public int getAge(){
        return age;
    }

    public static void have(){
        System.out.println("Every person has life ");
    }

    public void eat(){
        System.out.println(fname + "eats edible food");
    }
    public void walk(){
        System.out.println(fname + "walks in the road");
    }
    public void talk(){
        System.out.println(this.fname +" talks many languages");
    }
    public String toString(){
        return fname+" "+lname+age +" ";
    }
    public void showDetails(){
        System.out.println("fname"+fname);
        System.out.println("lname"+lname);
        System.out.println("Age"+age);

    }
}
