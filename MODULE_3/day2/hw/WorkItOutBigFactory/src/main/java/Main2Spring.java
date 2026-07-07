import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class Main2Spring {
    public static void main(String[] args) {
        ApplicationContext context =new  AnnotationConfigApplicationContext(MySpringConfiguration.class);
        NotificationService ns =context.getBean("wa",NotificationService.class);
        PaymentService ps =context.getBean("credit",PaymentService.class);
        ExpenseManager expenseManager = new ExpenseManager(ns,ps);
        expenseManager.payEbBill(100);
    }
}
