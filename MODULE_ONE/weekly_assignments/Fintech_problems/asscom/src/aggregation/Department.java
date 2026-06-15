package aggregation;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private String hqlocation;
    private List<Employee> employees;

    Department(String name,String hqlocation){
        this.name=name;
        this.hqlocation=hqlocation;
        this.employees= new ArrayList<>();

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getHqlocation() {
        return hqlocation;
    }

    public void setHqlocation(String hqlocation) {
        this.hqlocation = hqlocation;
    }
    public void addEmployee(Employee e){
        employees.add(e);
    }

    public void removeEmployeefirst(){
        employees.remove(0);
    }

    public void removeEmployeelast(){//madatory to check this is not empey ..
        employees.remove(employees.size()-1);
    }

    public void removeEmployee(String name){


            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getName().equals(name)) {
                    employees.remove(i);
                    break;
                }
            }


    }
    public List<Employee> getEmployees() {
        return employees;
    }
}
