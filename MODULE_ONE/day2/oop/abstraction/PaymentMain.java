import java.util.*;

class PaymentMain{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the type of payment \n 1 for upi\n2 for debit card \n 3 for credit card");
        int val=sc.nextInt();
        System.out.println("Enter the amount");
        double amount=sc.nextDouble();
        
        Payment p1;
        switch(val){
            case 1:
                p1= new Upi();
                break;
            case 2:
                p1=new DebitCard();
                break;
            case 3:
                p1=new CreditCard();
            default:
                System.out.println("Enter the correct type");
                return;
            
        }
        p1.things_needed();
        p1.limit();
        p1.paid(amount);
    }
}
}