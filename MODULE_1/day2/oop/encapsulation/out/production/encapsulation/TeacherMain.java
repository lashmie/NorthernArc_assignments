public class TeacherMain {
public static void main(String[] args) {

    Teacher t1 = new Teacher("Mr. John", "Math", 10);
    Teacher t2 = new Teacher("Mrs. Anaya", "Science", 8);
    Teacher t3 = new Teacher("Mr. Ali", "English", 5);

    t1.teach();
    t1.getDetails();

    System.out.println();

    t2.teach();
    t2.getDetails();

    System.out.println();

    t3.teach();
    t3.getDetails();
}

}