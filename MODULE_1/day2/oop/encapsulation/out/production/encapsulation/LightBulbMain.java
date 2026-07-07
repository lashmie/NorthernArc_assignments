public class LightBulbMain {
public static void main(String[] args) {

    LightBulb l1 = new LightBulb(9, "Philips");
    LightBulb l2 = new LightBulb(12, "Syska");
    LightBulb l3 = new LightBulb(15, "Havells");

    l1.turnOn();
    l1.getDetails();
    l1.turnOff();

    System.out.println();

    l2.turnOn();
    l2.getDetails();
    l2.turnOff();

    System.out.println();

    l3.turnOn();
    l3.getDetails();
    l3.turnOff();
}

}