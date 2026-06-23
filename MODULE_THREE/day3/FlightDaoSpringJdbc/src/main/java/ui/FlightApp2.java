package ui;


import Config.FlightConfig;
import controller.ConsoleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class FlightApp2 {
    public static void main(String[] args) {
        ApplicationContext context=new AnnotationConfigApplicationContext(FlightConfig.class);
        ConsoleController controller=context.getBean("ccc",ConsoleController.class);
        controller.showMenu();

    }
}
