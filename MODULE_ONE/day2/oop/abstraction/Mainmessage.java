import java.util.*;
public class Mainmessage{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the message");
        String message = sc.nextLine();
        System.out.println("Enter the choice \n1.for wa message\n2.for simple message\n3.for email message");
        int choice=sc.nextInt();
        message m1;
        switch(choice){
            case 1:
                m1= new Wamessage();
                break;
            case 2:
                m1= new Simplemessage();
                break;
            case 3:
                m1= new Emailmessage();
                break;
            default:
                System.out.println("Enter the valid choic");
                return;



        }
        m1.send(message);
    }
}