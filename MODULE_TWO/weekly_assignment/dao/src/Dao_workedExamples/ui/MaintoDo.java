package Dao_workedExamples.ui;

import Dao_workedExamples.dao.todoDao;
import Dao_workedExamples.dao.todoImpl;
import Dao_workedExamples.entity.todo;

import java.util.Scanner;

public class MaintoDo {
    public static void main(String[] args) {
        todoDao t = new todoImpl();
        //List<todo> list=List.of(new todo(1,"hw"),new todo(2,"learn extra"));
        System.out.println("Enter 1 save task\n 2.findById 3.update 4.deletebyid 6.deleteall 7.findall Enter 5.toexit\n");
        Scanner sc = new Scanner(System.in);
        int val = sc.nextInt();
    int a=4;
        while (a!=5) {
            switch (val) {
                case 1:
                    System.out.println("Enter the id");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter the task");
                    String task = sc.nextLine();
                    t.save(new todo(id, task));
                    System.out.println("task saved");
                    break;
                case 2:
                    System.out.println("Enter the id");
                    int id1 = sc.nextInt();
                    sc.nextLine();
                    System.out.println(t.findbyId(id1));
                    break;
                case 3:

                    System.out.println("updated the task");
                case 4:
                    System.out.println("Enter the id");
                    int id2 = sc.nextInt();
                    sc.nextLine();
                    t.deletebyid(id2);
                    System.out.println("deleted");
                    break;
                case 5:
                    a=5;
                    System.out.println("Exiting.....");

                case 6:
                    t.deleteall();
                    System.out.println("all deleted");
                    break;
                case 7:
                    System.out.println("The works are :");
                    t.findall();
                    break;

                default:
                    System.out.println("Invalid choice");
                    return;

            }
        }
    }
}
