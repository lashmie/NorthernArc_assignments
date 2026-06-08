package demo;
import java.util.Scanner;

public class ManuallyThrow {
    public static void main(String[] args) {
        System.out.println("Enter ur name");
        Scanner sc = new Scanner(System.in);
        String name = sc.next();
        try{
            if(!name.equals("ezhil") && !name.equals("Mohi") ){
                throw new ArithmeticException();

            }
            System.out.println("Welcome"+name);
        }
        catch (Exception e){
            System.out.println("Some error happened");
        }
    }
}
