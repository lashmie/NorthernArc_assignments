package Services;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ApplicationContext context=new ClassPathXmlApplicationContext("ApplicationContext.xml");

//        Error here  :: different variable names
//        System.out.println("Enter type of the payment:credit ,debit ,upi");
//        String ns=sc.next();
//        System.out.println("Enter type of the notification");
//        String ps =sc.next();

        ExpenseManager expense = context.getBean(ExpenseManager.class);
        expense.gasbill(1000);
        expense.ebBill(3000);

    }
}
