public class ArtistMain {
public static void main(String[] args) {

    Artist a1 = new Artist("Leonardo", "Painting", 15);
    Artist a2 = new Artist("Rahul", "Singing", 8);
    Artist a3 = new Artist("Meera", "Dancing", 10);

    a1.perform();
    a1.getDetails();

    System.out.println();

    a2.perform();
    a2.getDetails();

    System.out.println();

    a3.perform();
    a3.getDetails();
}

}