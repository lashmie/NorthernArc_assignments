package BookDemo.ui;

import BookDemo.Config.Config;
import BookDemo.Controller.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.ObjectInputFilter;

public class BookMain2{
    public static void main(String[] args) {
        ApplicationContext context=new AnnotationConfigApplicationContext(Config.class);
        Controller controller= context.getBean(Controller.class);
        controller.ShowMenu();
    }}