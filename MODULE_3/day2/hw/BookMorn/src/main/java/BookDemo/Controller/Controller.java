package BookDemo.Controller;

import BookDemo.dao.BookDao;
import BookDemo.entity.Book;

import java.util.List;
import java.util.Scanner;

public class Controller {
    private Scanner scanner;
    private BookDao book;
    public Controller(Scanner scanner,BookDao book){
        this.scanner=scanner;
        this.book=book;
    }
       public void ShowMenu(){
        int choice;
           System.out.println("Enter 1 for Save");
           System.out.println("Enter 2 for Update");
           System.out.println("Enter 3 for Find By Id");
           System.out.println("Enter 4 for Find By Name");
           System.out.println("Enter 5 for Find All");
           System.out.println("Enter 6 for Delete All");
           System.out.println("Enter 7 for Delete By Id");
           System.out.println("Enter 8 for Delete By Name");
           System.out.println("Enter 9 for Exit");
           do{
               System.out.println("Enter choice:");
               choice=scanner.nextInt();
               redirect(choice);
           }while(true);
       }
       public void redirect(int choice){
           switch(choice) {
               case 1 -> save();
               case 2 -> update();
               case 3 -> findById();
               case 4 -> findByName();
               case 5 -> findAll();
               case 6 -> deleteAll();
               case 7 -> deleteById();
               case 8 -> deleteByName();
               case 9 -> System.out.println("Exit");
               default -> System.out.println("Invalid option");
           }
       }
       public void save(){
           System.out.println("Enter the book id");
           int id=scanner.nextInt();
           System.out.println("Enter the book name");
           String name =scanner.next();
           System.out.println("Enter the book price");
           Double price = scanner.nextDouble();
           book.save(new Book(name,id,price));
           System.out.println("Book is saved....");
       }
    public void update() {
        System.out.println("Enter the book id to update");
        int id = scanner.nextInt();

        System.out.println("Enter new book name");
        String name = scanner.next();

        System.out.println("Enter new book price");
        double price = scanner.nextDouble();

        book.update(new Book( name,id, price));

        System.out.println("Book is updated....");
    }
    public void findById() {
        System.out.println("Enter book id");
        int id = scanner.nextInt();

        Book b = book.findById(id);

        if (b != null) {
            System.out.println(b);
        } else {
            System.out.println("Book not found");
        }
    }
    public void findByName() {
        System.out.println("Enter book name");
        String name = scanner.next();

        Book b = book.findByname(name);

        if (b != null) {
            System.out.println(b);
        } else {
            System.out.println("Book not found");
        }
    }
    public void findAll() {
        List<Book> list = book.findAll();

        if (list.isEmpty()) {
            System.out.println("No books found");
        } else {
            for (Book b : list) {
                System.out.println(b);
            }
        }
    }
    public void deleteAll() {
        book.deleteAll();
        System.out.println("All books deleted....");
    }
    public void deleteById() {
        System.out.println("Enter book id to delete");
        int id = scanner.nextInt();

        book.deleteById(id);

        System.out.println("Book deleted (if existed)");
    }
    public void deleteByName() {
        System.out.println("Enter book name to delete");
        String name = scanner.next();

        book.deletebyName(name);

        System.out.println("Book deleted (if existed)");
    }
}
