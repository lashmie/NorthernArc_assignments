import java.util.Scanner;

public class switchs{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("enter the date");
        int date = sc.nextInt();
        calculate(date);

    }
    private static void calculate(int date){
        int val=date %7;
        switch(val){
            case 1:
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            case 3:
                System.out.println("Wednesday");
                break;
            case 4:
                System.out.println("Thursday");
                break;
            case 5:
                System.out.println("friday");
                break;
            case 6:
                System.out.println("Saturday");
                break;
            default:
                System.out.println("Sunday");

        }
    }


}
