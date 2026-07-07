package Dao_workedExamples.ui;

import Dao_workedExamples.dao.PersonDao;
import Dao_workedExamples.dao.PersonImpl;
import Dao_workedExamples.entity.Person;

import java.util.Scanner;

public class PersonApp {
    public static void main(String[] args) {

        PersonDao p = new PersonImpl();
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {

            System.out.println("\n===== PERSON MENU =====");
            System.out.println("1. Save Person");
            System.out.println("2. Find Person by Id");
            System.out.println("3. Update Person");
            System.out.println("4. Delete Person by Id");
            System.out.println("5. Find All Persons");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter id: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter age: ");
                    int age = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter education: ");
                    String education = sc.nextLine();

                    p.save(new Person( name, age, education));
                    System.out.println("Person saved");
                    break;

//                case 2:
//                    System.out.print("Enter id: ");
//                    int id1 = sc.nextInt();
//
//                    System.out.println(p.findById(id1));
//                    break;

                case 3:
                    System.out.print("Enter id: ");
                    int uid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new name: ");
                    String uname = sc.nextLine();

                    System.out.print("Enter new age: ");
                    int uage = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new education: ");
                    String uedu = sc.nextLine();

                    p.update(new Person( uname, uage, uedu));
                    System.out.println("Person updated");
                    break;

//                case 4:
//                    System.out.print("Enter id: ");
//                    int did = sc.nextInt();
//
//                    p.deleteById(did);
//                    System.out.println("Person deleted");
//                    break;

                case 5:
                    System.out.println("All Persons:");
                    System.out.println(p.findAll());
                    break;

                case 0:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}