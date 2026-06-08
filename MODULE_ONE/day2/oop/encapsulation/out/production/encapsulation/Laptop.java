public class Laptop {
private String brand;
private String model;
private String processor;
private String ram;
private String storage;


public Laptop(String brand, String model, String processor, String ram, String storage) {
    this.brand = brand;
    this.model = model;
    this.processor = processor;
    this.ram = ram;
    this.storage = storage;
}

public void turnOn() {
    System.out.println(brand + " " + model + " is turning ON");
}

public void turnOff() {
    System.out.println(brand + " " + model + " is turning OFF");
}

public void getDetails() {
    System.out.println(
        "Brand:" + brand + "\n" +
        "Model:" + model + "\n" +
        "Processor: " + processor + "\n" +
        "RAM: " + ram + "\n" +
        "Storage: " + storage + "\n"
    );
}


}