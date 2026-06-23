package demo;




import demo.controller.TodoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class ui implements CommandLineRunner {
    @Autowired
    TodoController todoController;
    public static void main(String[] args) {

        SpringApplication.run(ui.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Program is running..........");
        todoController.showMenu();
    }
}
