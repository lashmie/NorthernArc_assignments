package paymentDemo2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Scanner;
@Configuration
@ComponentScan(basePackages = "paymentDemo2")
public class Main {
    @Bean
    public Scanner getSc(){
        return new Scanner(System.in);
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ApplicationContext context=new AnnotationConfigApplicationContext(Main.class);
//        ExpenseManager expense = context.getBean(ExpenseManager.class);
//        expense.gasbill(1000);
//        expense.ebBill(3000);
        ConsoleController consoleController=context.getBean(ConsoleController.class);
        consoleController.showMenu();

    }
}
