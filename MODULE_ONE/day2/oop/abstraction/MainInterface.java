import java.util.*;

class MainInterface{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the type of payment \n 1 for upi\n2 for debit card \n 3 for credit card");
        int val=sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the amount");
        double amount=sc.nextDouble();

        PaymentInterface p1;
        switch(val){
            case 1:
                p1= new UpiPaymentIn();
                break;
            case 2:
                p1=new DebitPaymentIn();
                break;
            case 3:
                p1=new CreditPaymentIn();
            default:
                System.out.println("Enter the correct type");
                return;
            
        }
        p1.payment(amount);
    }
}


       
        
   