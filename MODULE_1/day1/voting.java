import java.util.Scanner;
public class voting {
    public static void main(String[] args) {
       Scanner sc = new Scanner(System.in);
        int age =sc.nextInt();
        check(age);
    }
    private static void check(int age){
        if(age >18){
            System.out.println("You can vote");
        }
        else{
            System.out.println("you cannot vote");
        }
        
    }
}
