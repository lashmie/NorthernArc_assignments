public class AthleteMain {
public static void main(String[] args) {

    Athlete a1 = new Athlete("Neeraj Chopra", "Javelin Throw", 3);
    Athlete a2 = new Athlete("PV Sindhu", "Badminton", 5);
    Athlete a3 = new Athlete("Virat Kohli", "Cricket", 10);

    a1.play();
    a1.getDetails();

    System.out.println();

    a2.play();
    a2.getDetails();

    System.out.println();

    a3.play();
    a3.getDetails();
}

}