package composition;

public class Main {
    public static void main(String[] args) {
        Car c1 = new Car("bmw",new Engine(90));
        c1.start();
    }
}
