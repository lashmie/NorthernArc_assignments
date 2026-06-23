package paymentDemo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ApplicationContext context=new AnnotationConfigApplicationContext(MySpringConfiguration.class);

//        Error here  :: different variable names
//        System.out.println("Enter type of the payment:credit ,debit ,upi");
//        String ns=sc.next();
//        System.out.println("Enter type of the notification");
//        String ps =sc.next();

        System.out.println("Enter type of the payment:credit ,debit ,upi");
        String ps=sc.next();
        System.out.println("Enter type of the notification");
        String ns =sc.next();

        NotificationService not = context.getBean(ns,NotificationService.class);//creditcard.class
        PaymentService pot=context.getBean(ps,PaymentService.class);

        ExpenseManager expense = context.getBean(ExpenseManager.class);
        expense.gasbill(1000);
        expense.ebBill(3000);

    }
}
