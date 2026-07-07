public class Employee {
    private String name;
    private String position;
    private  int salary;
    public Employee(String name,String position,int salary){
        this.name=name;
        this.position=position;
        this.salary=salary;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setPosition(String pos){
        this.position=pos;
    }
    public void setSalary(int sal){
        this.salary=sal;
    }

    public String getName(){
        return name;
    }
    public String getPosition(){
        return position;
    }
    public int getSalary(){
        return salary;
    }
    public void showEmployeeDetails(){
        System.out.println("The name of the employee is "+ name + " and he is holding the position "+ position + " and his salary is "+ salary);
    }
}
