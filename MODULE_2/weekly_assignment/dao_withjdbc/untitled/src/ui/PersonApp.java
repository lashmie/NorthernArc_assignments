package ui;


import dao.PersonDao;
import dao.PersonDaoImpl;
import entity.Person;

public class PersonApp {

    public static void main(String[] args) {

        PersonDao dao = new PersonDaoImpl();

        // Save persons
        Person p1 = new Person("John", 25, "B.Tech");
        Person p2 = new Person("David", 30, "MCA");

        dao.save(p1);
        dao.save(p2);

        // Display all persons
        System.out.println("\n===== ALL PERSONS =====");
        dao.findAll().forEach(System.out::println);

        // Update John
        Person updatedPerson = new Person("John", 26, "M.Tech");
        dao.update(updatedPerson);

        System.out.println("\n===== AFTER UPDATE =====");
        dao.findAll().forEach(System.out::println);

        // Delete David
        dao.deleteById("David");

        System.out.println("\n===== AFTER DELETE =====");
        dao.findAll().forEach(System.out::println);

        // Delete all records
        // dao.deleteAll();

        // System.out.println("\n===== AFTER DELETE ALL =====");
        // dao.findAll().forEach(System.out::println);
    }
}