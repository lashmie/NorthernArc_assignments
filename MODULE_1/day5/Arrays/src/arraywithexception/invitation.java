package arraywithexception;
import java.util.*;

public class invitation {

    static void check(String name,String[]arr)throws CustomException{
    for(String val:arr){
        if(val.equals(name)){
            System.out.println("welcome to the party "+name);
            return;

        }
    }
        throw new CustomException("you are not in the list");
    }

    public static void main(String[] args) {

        String arr[]= new String[3];
        arr[0]="lava";
        arr[1]="lash";
        arr[2]="elava";

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name");
        String name=sc.next();
        try{
            check(name,arr);
        }
        catch (CustomException e){
            System.out.println(e.getMessage());
        }

    }
}

