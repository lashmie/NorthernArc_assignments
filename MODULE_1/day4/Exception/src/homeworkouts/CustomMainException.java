package homeworkouts;
import demo.CustomException;

import java.util.Scanner;
public class CustomMainException {
    static void check(String name ,String password) throws CustomExcept {
        if(!name.equalsIgnoreCase("Lavanya") || !password.equals("Lava@@01")){
            throw new CustomExcept("invalid username and password");


        }
    }

    public static void main(String[] args){


        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name");
        String name = sc.next();
        System.out.println("Enter your password");
        String password=sc.next();

try{
    check(name,password);
}
catch (CustomExcept e){
    System.out.println(e.getMessage());
}

            sc.close();


    }
}
