package ui;
import dao.todoDao;
import dao.todoImpl;
import entity.todo;

public class MaintoDo {

    public static void main(String[] args) {

        todoDao dao = new todoImpl();

        // Insert
        todo t1 = new todo(1, "Learn JDBC");
        todo t2 = new todo(2, "Finish DAO Project");

        dao.save(t1);
        dao.save(t2);

        // Display all
        System.out.println("\nAll Todos:");
        dao.findall();

        // Find by id
        System.out.println("\nTodo with id 1:");
        System.out.println(dao.findbyId(1));

        // Update
        todo updated = dao.findbyId(1);

        if (updated != null) {
            updated.setCompleted(true);
            updated.setTask("Learn JDBC Completely");

            dao.update(updated);
        }

        System.out.println("\nAfter Update:");
        dao.findall();

        // Delete one
        dao.deletebyid(2);

        System.out.println("\nAfter deleting id 2:");
        dao.findall();

        // Delete all
        // dao.deleteall();
    }
}