public class CarMain {
    public static void main(String[] args) {
        Car c1 = new Car("Toyota", "Corolla", "2020");
        Car c2 = new Car("Honda", "Civic", "2022");
        Car c3 = new Car("Hyundai", "i20", "2021");

        c1.start();
        c1.getDetails();
        c1.stop();
        System.out.println();

        c2.start();
        c2.getDetails();
        c2.stop();
        System.out.println();

        c3.start();
        c3.getDetails();
        c3.stop();
    }
}
