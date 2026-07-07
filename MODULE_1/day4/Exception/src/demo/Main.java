package demo;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try{

            System.out.println("Enter the number a : ");
            int a =sc.nextInt();
            System.out.println("Enter the numner b : ");
            int b =sc.nextInt();
            double result = a/b;
            System.out.println("The result"+result);

        }
        catch(InputMismatchException e){
            System.out.println("Enter the valid number");
        }
        catch (ArithmeticException e){
            System.out.println("Pls enter valid numbers");
        }
        catch(Exception e){
            System.out.println("something went wrong");
        }
        finally {
            System.out.println("this is finally block");
            sc.close();
        }
        System.out.println("hello world");

    }
}
