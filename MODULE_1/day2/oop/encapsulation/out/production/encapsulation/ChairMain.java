public class ChairMain {
public static void main(String[] args) {

    Chair c1 = new Chair("Wood", "Brown", 7.5);
    Chair c2 = new Chair("Plastic", "White", 4.2);
    Chair c3 = new Chair("Metal", "Black", 6.8);

    c1.adjustHeight();
    c1.getDetails();

    System.out.println();

    c2.adjustHeight();
    c2.getDetails();

    System.out.println();

    c3.adjustHeight();
    c3.getDetails();
}

}