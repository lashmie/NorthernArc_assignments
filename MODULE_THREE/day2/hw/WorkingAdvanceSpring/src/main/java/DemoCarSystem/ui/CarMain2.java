package DemoCarSystem.ui;

import DemoCarSystem.Controller.CarController;
import DemoCarSystem.Services.Car;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "DemoCarSystem")
public class CarMain2 {
    @Bean
    public Scanner getSc() {
        return new Scanner(System.in);
    }
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(CarMain.class);
        CarController carController=context.getBean(CarController.class);
        carController.showMenu();
    }

}