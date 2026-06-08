import java.util.Scanner;

public class PaymentMain{
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
      
        Payment s1;
        System.out.println("Enter the amount:");
        double amount = sc.nextDouble();
        System.out.println("Enter 1 for upi, enter 2 for debit ,enter 3 for credit ");
        int choice = sc.nextInt();
         
        switch(choice) {
            case 1:
                s1= new Upi();
                s1.send(amount);
                break;
            case 2:
                s1= new Debit();
                s1.send(amount);
                break;
            case 3:
                s1=new Credit();
                s1.send(amount);
        
            default:
                break;
        }
       

    }
}
