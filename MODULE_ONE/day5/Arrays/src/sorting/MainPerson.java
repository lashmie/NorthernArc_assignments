package sorting;
import java.sql.SQLOutput;
import java.util.*;

public class MainPerson {
    public static void main(String[] args) {
    class lname implements Comparator<Person>{
        public int compare(Person p1,Person p2){
            return p1.lname.compareTo(p2.lname);
        }
    }
    Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number the way u wanna sort 1.name  2.age asc 3.age desc\n");
        int val= sc.nextInt();
        Person arr[] = new Person[3];
        Person p1= new Person("lavanya",21,"abi");
        Person p2 = new Person("Mohana",18,"vis");
        Person p3 = new Person("lavanya",19,"balu");
        arr[0]=p1;
        arr[1]=p2;
        arr[2]=p3;
//        Arrays.sort(arr);

        switch (val){
            case 1:
                Arrays.sort(arr);
                break;
            case 2:
                Arrays.sort(arr,new AscAgeComparator());
                break;

            case 3:
                Arrays.sort(arr,new desAgeComparator());
                break;
            case 4:
                Arrays.sort(arr,new lname());
            case 5:
                Arrays.sort(arr,new Comparator<Person>(){
                    public int compare(Person p1,Person p2){
                    return p2.lname.compareTo(p1.lname);
                    }
            });
            default:
                System.out.println("Enter a valid number");
                return;

        }

        System.out.println(Arrays.toString(arr));

    }
}
