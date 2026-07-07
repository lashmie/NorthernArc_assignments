package ui;
//
//import Config.SpringConfiguration;
//import dao.DaoTodo;
//import dao.DaoTodoImplColl;
//import entity.todo;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//public class TodoApp {
//    public static void main(String[] args) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
//        DaoTodo todo = context.getBean(DaoTodoImplColl.class);
//        todo.save(new todo(1,"Yogo",false));
//        todo.save(new todo(2,"Eat Fruits",true));
//
//        System.out.println("Find ID 1: " + todo.findbyId(1));
//        System.out.println("All Todos: " + todo.findall());
//
//        // UPDATE (example: replace task for id 1)
//        todo updated = new todo(1, "Learn Java Deeply",true);
//        todo.update(updated);
//        System.out.println("After update: " + todo.findbyId(1));
//
//        todo.deletebyid(2);
//        System.out.println("After delete id 2: " + todo.findall());
//
//    }
//    }
//
import Controller.TodoConsoleController;
import dao.DaoTodo;
import dao.DaoTodoImplColl;

import java.util.Scanner;

public class TodoApp {

    public static void main(String[] args) {

        // 1. Create dependencies
        Scanner scanner = new Scanner(System.in);
        DaoTodo dao = new DaoTodoImplColl();

        // 2. Inject into controller
        TodoConsoleController controller =
                new TodoConsoleController(scanner, dao);

        // 3. Start app
        controller.printWelcomeMessage();
        controller.showMenu();
    }
}