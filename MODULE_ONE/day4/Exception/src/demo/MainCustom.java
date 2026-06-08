package demo;
import java.util.Scanner;
public class MainCustom {
    public static void main(String[] args) {
       Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name");
        String name= sc.next();
        try{
            if(!name.equalsIgnoreCase("Mugilan")){
                throw new CustomException("name not allowded"+name);
            }
            System.out.println("Welcome"+name);
        }
        catch(CustomException e){
            System.out.println("u are not allowded to the party");
        }
    }
}
