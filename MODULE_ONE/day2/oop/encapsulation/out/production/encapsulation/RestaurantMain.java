public class RestaurantMain {
public static void main(String[] args) {

    Restaurant r1 = new Restaurant("Pizza Hut", "Italian", 4);
    Restaurant r2 = new Restaurant("KFC", "Fast Food", 5);
    Restaurant r3 = new Restaurant("Bawarchi", "Indian", 4);

    r1.serveFood();
    r1.getDetails();

    System.out.println();

    r2.serveFood();
    r2.getDetails();

    System.out.println();

    r3.serveFood();
    r3.getDetails();
}

}