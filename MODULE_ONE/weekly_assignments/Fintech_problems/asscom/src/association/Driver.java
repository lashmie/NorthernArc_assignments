package association;

public class Driver {
    private String name;
    Driver(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }

    public void drive(Car car){
        System.out.println("The driver"+ name+"is driving"+car.getBrand());
    }
}
