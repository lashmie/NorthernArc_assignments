package demo.controller;

import demo.dao.TodoDao;
import demo.dao.TodoDaoImpl;
import demo.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;
@Service
public class TodoController {
    @Autowired
    private TodoDao todo;
    @Autowired
    private Scanner scanner;
    int choice;
    public void showMenu(){
        while(true){
            System.out.println("Enter 1 for save ");
            System.out.println("Enter 2 for delete");
            System.out.println("Enter 3 for find");
            System.out.println("Enter 4 for exit.");
            System.out.println("Enter the Choice : ");
            choice= scanner.nextInt();
            redirect(choice);
        }

    }
    private void redirect(int choice){
        switch (choice){
            case 1 ->{
                save();
            }
            case 2->{
                delete();
            }
            case 3->{
                find();
            }
            case 4->{
                System.exit(0);
            }
        }
    }
    private void save(){
        System.out.println("Enter the id : ");
        int id =scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter the content : ");
        String content=scanner.nextLine();
        scanner.nextLine();
        System.out.println("Enter the status");
        Boolean val=scanner.nextBoolean();
        Todo t = new Todo(id,content,val);
        todo.save(t);
        System.out.println("todo saved successfully........");

    }
    private void delete(){
        System.out.println("Enter the id");
        int id =scanner.nextInt();
        todo.deleteById(id);
        System.out.println("deleted..........");
    }
    private void find(){
        System.out.println("Enter the id");
        int id=scanner.nextInt();
        todo.findById(id);
        System.out.println("The todo is founded");
    }

}
