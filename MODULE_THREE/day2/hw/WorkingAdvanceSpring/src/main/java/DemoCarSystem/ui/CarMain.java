package DemoCarSystem.ui;

import DemoCarSystem.Services.Car;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "DemoCarSystem")
public class CarMain {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(CarMain.class);
        Car car = context.getBean(Car.class);
        car.drive();
    }

}
