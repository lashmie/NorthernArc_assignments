package ui;

import dao.StudentDao;
import dao.StudentImpl;
import entity.Student;

import java.util.Scanner;

public class StudentApp1 {

    public static void main(String[] args) {

        StudentDao dao = new StudentImpl();

        // Sample Data
        dao.addStudent(new Student("Arun", 90, 85, 95, 88, 99, 92));
        dao.addStudent(new Student("Priya", 95, 90, 99, 91, 98, 94));
        dao.addStudent(new Student("Kumar", 80, 75, 88, 84, 90, 86));
        dao.addStudent(new Student("Divya", 92, 89, 97, 95, 96, 93));

        Scanner sc = new Scanner(System.in);

        int choice;

        do {

            System.out.println("\n===== STUDENT MANAGEMENT =====");
            System.out.println("1. Display All Students");
            System.out.println("2. Student Count");
            System.out.println("3. Maximum Mark in Subject");
            System.out.println("4. Subject Topper");
            System.out.println("5. Subject Average");
            System.out.println("6. Count Students Above Subject Average");
            System.out.println("7. Class Topper");
            System.out.println("8. Exit");
            System.out.print("Enter Choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.println("\n===== STUDENTS =====");

                    dao.getAllStudents()
                            .forEach(student ->
                                    System.out.println(student.getName()));

                    break;

                case 2:

                    System.out.println("Total Students : "
                            + dao.getStudentCount());

                    break;

                case 3:

                    System.out.print("Enter Subject : ");
                    String maxSubject = sc.nextLine();

                    System.out.println("Maximum Mark : "
                            + dao.getMaxMark(maxSubject));

                    break;

                case 4:

                    System.out.print("Enter Subject : ");
                    String topperSubject = sc.nextLine();

                    Student topper =
                            dao.getTopper(topperSubject);

                    System.out.println("Topper : "
                            + topper.getName());

                    break;

                case 5:

                    System.out.print("Enter Subject : ");
                    String avgSubject = sc.nextLine();

                    System.out.println("Average : "
                            + dao.getAverage(avgSubject));

                    break;

                case 6:

                    System.out.print("Enter Subject : ");
                    String aboveAvgSubject = sc.nextLine();

                    System.out.println(
                            "Students Above Average : "
                                    + dao.countStudentsAboveAverage(
                                    aboveAvgSubject));

                    break;

                case 7:

                    Student classTopper =
                            dao.getClassTopper();

                    int total =
                            classTopper.getEnglish()
                                    + classTopper.getTamil()
                                    + classTopper.getPhysics()
                                    + classTopper.getChemistry()
                                    + classTopper.getMaths()
                                    + classTopper.getBiology();

                    System.out.println("Class Topper : "
                            + classTopper.getName());

                    System.out.println("Total Marks : "
                            + total);

                    break;

                case 8:

                    System.out.println("Thank You...");
                    break;

                default:

                    System.out.println("Invalid Choice");
            }

        } while (choice != 8);

        sc.close();
    }
}
