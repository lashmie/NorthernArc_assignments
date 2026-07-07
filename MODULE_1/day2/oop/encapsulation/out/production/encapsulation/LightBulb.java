public class LightBulb {
private int watts;
private String brand;
private String state;

public LightBulb(int watts, String brand) {
    this.watts = watts;
    this.brand = brand;
    this.state = "OFF";
}

public void turnOn() {
    state = "ON";
    System.out.println(brand + " light bulb is TURNED ON");
}

public void turnOff() {
    state = "OFF";
    System.out.println(brand + " light bulb is TURNED OFF");
}

public void getDetails() {
    System.out.println(
        "Brand: " + brand + "\n" +
        "Watts: " + watts + "\n" +
        "State: " + state + "\n"
    );
}

public int getWatts() {
    return watts;
}

public void setWatts(int watts) {
    this.watts = watts;
}

public String getBrand() {
    return brand;
}

public void setBrand(String brand) {
    this.brand = brand;
}

}