package ui;



import dao.StudentDao;
import dao.StudentImpl;
import entity.Student;

public class StudentApp {

    public static void main(String[] args) {

        StudentDao dao = new StudentImpl();

        // Add Students
        dao.addStudent(new Student(
                "Arun", 90, 85, 95, 88, 99, 92));

        dao.addStudent(new Student(
                "Priya", 95, 90, 99, 91, 98, 94));

        dao.addStudent(new Student(
                "Kumar", 80, 75, 88, 84, 90, 86));

        dao.addStudent(new Student(
                "Divya", 92, 89, 97, 95, 96, 93));

        // Display all students
        System.out.println("===== STUDENT LIST =====");

        dao.getAllStudents().forEach(student ->
                System.out.println(student.getName()));

        // Student Count
        System.out.println("\nTotal Students : "
                + dao.getStudentCount());

        // Physics
        System.out.println("\n===== PHYSICS =====");

        System.out.println("Maximum Physics Mark : "
                + dao.getMaxMark("physics"));

        System.out.println("Physics Topper : "
                + dao.getTopper("physics").getName());

        System.out.println("Physics Average : "
                + dao.getAverage("physics"));

        System.out.println("Students Above Physics Average : "
                + dao.countStudentsAboveAverage("physics"));

        // Maths
        System.out.println("\n===== MATHS =====");

        System.out.println("Maximum Maths Mark : "
                + dao.getMaxMark("maths"));

        System.out.println("Maths Topper : "
                + dao.getTopper("maths").getName());

        System.out.println("Maths Average : "
                + dao.getAverage("maths"));

        // English
        System.out.println("\n===== ENGLISH =====");

        System.out.println("Maximum English Mark : "
                + dao.getMaxMark("english"));

        System.out.println("English Topper : "
                + dao.getTopper("english").getName());

        System.out.println("English Average : "
                + dao.getAverage("english"));

        // Class Topper
        Student classTopper = dao.getClassTopper();

        System.out.println("\n===== CLASS TOPPER =====");

        System.out.println("Name : "
                + classTopper.getName());

        int total =
                classTopper.getEnglish()
                        + classTopper.getTamil()
                        + classTopper.getPhysics()
                        + classTopper.getChemistry()
                        + classTopper.getMaths()
                        + classTopper.getBiology();

        System.out.println("Total Marks : "
                + total);

        System.out.println("\n===== COLLECTORS =====");

// counting()
        System.out.println("Count : " +
                dao.getAllStudents()
                        .stream()
                        .collect(Collectors.counting()));

// averagingInt()
        System.out.println("Average Physics : " +
                dao.getAllStudents()
                        .stream()
                        .collect(Collectors.averagingInt(Student::getPhysics)));

// summingInt()
        System.out.println("Total Physics Marks : " +
                dao.getAllStudents()
                        .stream()
                        .collect(Collectors.summingInt(Student::getPhysics)));

// groupingBy()
        System.out.println("Group By Physics >= 90 : " +
                dao.getAllStudents()
                        .stream()
                        .collect(Collectors.groupingBy(
                                s -> s.getPhysics() >= 90
                                        ? "Above90"
                                        : "Below90")));

// partitioningBy()
        System.out.println("Partition By Maths >= 90 : " +
                dao.getAllStudents()
                        .stream()
                        .collect(Collectors.partitioningBy(
                                s -> s.getMaths() >= 90)));

// joining()
        System.out.println("Names : " +
                dao.getAllStudents()
                        .stream()
                        .map(Student::getName)
                        .collect(Collectors.joining(", ")));

// summarizingInt()
        System.out.println("Physics Summary : " +
                dao.getAllStudents()
                        .stream()
                        .collect(Collectors.summarizingInt(
                                Student::getPhysics)));
    }
}


