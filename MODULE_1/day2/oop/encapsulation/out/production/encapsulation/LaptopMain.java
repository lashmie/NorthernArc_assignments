public class LaptopMain {
public static void main(String[] args) {

    Laptop l1 = new Laptop("Dell", "Inspiron", "i5", "8GB", "512GB SSD");
    Laptop l2 = new Laptop("HP", "Pavilion", "i7", "16GB", "1TB HDD");
    Laptop l3 = new Laptop("Apple", "MacBook Air", "M2", "8GB", "256GB SSD");
//first object
    System.out.println();
    l1.turnOn();
    l1.getDetails();
    l1.turnOff();
//second object
System.out.println();
    l2.turnOn();
    l2.getDetails();
    l2.turnOff();

 
//third object
// System.out.println();
    l3.turnOn();
    l3.getDetails();
    l3.turnOff();
}

}