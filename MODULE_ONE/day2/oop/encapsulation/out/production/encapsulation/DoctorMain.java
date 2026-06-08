public class DoctorMain {
public static void main(String[] args) {

    Doctor d1 = new Doctor("Dr. Smith", "Cardiologist", 10);
    Doctor d2 = new Doctor("Dr. Anaya", "Neurologist", 8);
    Doctor d3 = new Doctor("Dr. John", "Dermatologist", 5);

    d1.diagnose();
    d1.treat();
    d1.getDetails();

    System.out.println();

    d2.diagnose();
    d2.treat();
    d2.getDetails();

    System.out.println();

    d3.diagnose();
    d3.treat();
    d3.getDetails();
}

}