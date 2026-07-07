import java.util.Scanner;

public class postive {
     public static void main(String[] args) {
       Scanner sc = new Scanner(System.in);
        int num =sc.nextInt();
        check(num);
    }
    private static void check(int num){
        if(num<0){
            System.out.println("It is negative");
        }
        else{
            System.out.println("It is positive");
        }
        
    }
}
