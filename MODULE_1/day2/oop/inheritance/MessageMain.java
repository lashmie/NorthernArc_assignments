import java.util.Scanner;

public class MessageMain {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
      
        SimpleMessage s1;
        System.out.println("Enter the message:");
        String message = sc.nextLine();
        System.out.println("Enter 1 for wa message, enter 2 for email message ,enter 3 for mobile message ");
        int choice = sc.nextInt();
         
        switch(choice) {
            case 1:
                s1= new WaMessage();
                s1.send(message);
                break;
            case 2:
                s1= new EmailMessage();
                s1.send(message);
                break;
            case 3:
                s1=new MobileMessage();
                s1.send(message);
        
            default:
                break;
        }
       

    }
}
