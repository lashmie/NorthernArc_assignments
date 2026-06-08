public class Employee extends person {
    private String empId;
    private String department;

    public Employee(String fname,String lname,int age,String empid,String dept){
        super(fname,lname,age);
        this.empId=empid;
        this.department=dept;

    }

    public String getEmpid(){
        return empId;
       
    }
    public String getDepartment(){
        return department;
    }

    public void setEmpId(String empId){
        this.empId=empId;
    }
    public void setDepartment(String d){
        this.department=d;
    }
    public void work(){
        System.out.println(this.getFname()+"is working in the "+department+"department"+"and his age is "+age);
    }
    public void showDetails(){
        super.showDetails();
        System.out.println("employee id"+empId);
        System.out.println("Department"+department);
        System.out.println("--------------------");
    }
}
