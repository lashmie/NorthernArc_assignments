package daoArchitecture.ui;


import daoArchitecture.dao.TodoDao;
import daoArchitecture.dao.TodoDaoImpl;
import daoArchitecture.entity.Todo;

public class TodoApp {

    public static void main(String[] args) {

        TodoDao dao = new TodoDaoImpl();

        // Save
        dao.save(new Todo(1, "Learn Java", false));
        dao.save(new Todo(2, "Complete DAO Assignment", false));

        // Find All
        System.out.println("All Todos:");
        dao.findAll().forEach(System.out::println);

        // Find By Id
        System.out.println("\nFind Id 1:");
        System.out.println(dao.findById(1));

        // Update
        Todo updated = new Todo(1, "Learn Java Collections", true);
        dao.update(updated);

        System.out.println("\nAfter Update:");
        dao.findAll().forEach(System.out::println);

        // Delete By Id
        dao.deleteById(2);

        System.out.println("\nAfter Delete:");
        dao.findAll().forEach(System.out::println);

        // Delete All
        dao.deleteAll();

        System.out.println("\nAfter Delete All:");
        System.out.println(dao.findAll());
    }
}