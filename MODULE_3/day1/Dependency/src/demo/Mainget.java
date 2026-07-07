package demo;

import java.util.Scanner;

public class Mainget {
    public static void main(String[] args) {
        ExpenseManager1 e1 = new ExpenseManager1();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter payment type");
        String pt=sc.nextLine();
        sc.nextLine();
        System.out.println("Enter the message type");
        String et = sc.nextLine();
        PaymentService ps = BigFactory1.getPaymentService(pt);
        NotificatonService ns = BigFactory1.getNotificationService(et);


        e1.setNs(ns);
        e1.setPs(ps);
        e1.payElectricitybill(1000);
        e1.payGasbill(346);
        e1.payWaterbill(564);


    }
}
