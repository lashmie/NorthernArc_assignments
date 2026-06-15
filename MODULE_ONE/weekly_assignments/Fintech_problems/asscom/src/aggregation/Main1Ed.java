package aggregation;

public class Main1Ed {
    public static void main(String[] args) {
        Department itdept= new Department("It","Banglore");
        Department hrdept = new Department("hr","chennai");
        Employee e1= new Employee("lavanya");
        Employee e2 = new Employee("renjihta");
        Employee e3 = new Employee("yuva priya");

        itdept.addEmployee(e1);
        itdept.addEmployee(e2);
        itdept.addEmployee(e3);

        System.out.println("Emloyees in IT");
        System.out.println(itdept.getEmployees());

        Employee e4= new Employee("sachin");
        Employee e5 = new Employee("saurav");
        Employee e6 = new Employee("vipin");

        hrdept.addEmployee(e4);
        hrdept.addEmployee(e5);
        hrdept.addEmployee(e6);
        System.out.println("hrdept people:");
        System.out.println(hrdept.getEmployees());

        hrdept.removeEmployee("sachin");
        System.out.println(hrdept.getEmployees());
        hrdept.removeEmployeefirst();
//
        System.out.println(hrdept.getEmployees());
        System.out.println();
        Employee e7 = new Employee("vipin");
        hrdept.removeEmployeelast();

    }
}
