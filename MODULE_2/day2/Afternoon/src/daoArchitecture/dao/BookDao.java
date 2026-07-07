package daoArchitecture.dao;

import daoArchitecture.entity.Book;
//
public interface BookDao {
    public void save(Book boa);
    public Book findbyId(int id);
    public void deleteById(int id);
    public void update(Book book);
    public void deleteAll();
    public Iterable<Book> findAll();
    public Iterable<Book> findByAuthor(String author);
    public Iterable<Book> findByTitle(String title);
//    public Iterable<Book> sortByTitleAsc();
//    public Iterable<Book> sortByTitleDesc();
}

////package daoArchitecture.dao;
////
////
////import daoArchitecture.entity.Book;
////
////import java.util.Scanner;
////
////public class Main {
////    public static void main(String[] args) {
////
////        Scanner sc = new Scanner(System.in);
////        Book bookDao = new BookDaoImp();
////
////        while (true) {
////            System.out.println("\n===== BOOK MENU =====");
////            System.out.println("1. Save Book");
////            System.out.println("2. Find By Id");
////            System.out.println("3. Delete By Id");
////            System.out.println("4. Update Book");
////            System.out.println("5. Delete All");
////            System.out.println("6. Find All");
////            System.out.println("7. Find By Author");
////            System.out.println"8. Find By Title");
////            System.out.println("9. Exit");
////
////            System.out.print("Enter choice: ");
////            int choice = sc.nextInt();
////            sc.nextLine();
////
////            switch (choice) {
////
////                case 1:
////                    System.out.print("Enter Id: ");
////                    int id = sc.nextInt();
////                    sc.nextLine();
////
////                    System.out.print("Enter Title: ");
////                    String title = sc.nextLine();
////
////                    System.out.print("Enter Author: ");
////                    String author = sc.nextLine();
////
////                    bookDao.save(new Book(id, title, author));
////                    System.out.println("Book Saved");
////                    break;
////
////                case 2:
////                    System.out.print("Enter Id: ");
////                    id = sc.nextInt();
////
////                    Book book = bookDao.findbyId(id);
////
////                    if (book != null) {
////                        System.out.println(book);
////                    } else {
////                        System.out.println("Book Not Found");
////                    }
////                    break;
////
////                case 3:
////                    System.out.print("Enter Id: ");
////                    id = sc.nextInt();
////
////                    bookDao.deleteById(id);
////                    System.out.println("Book Deleted");
////                    break;
////
////                case 4:
////                    System.out.print("Enter Id: ");
////                    id = sc.nextInt();
////                    sc.nextLine();
////
////                    System.out.print("Enter New Title: ");
////                    title = sc.nextLine();
////
////                    System.out.print("Enter New Author: ");
////                    author = sc.nextLine();
////
////                    bookDao.update(new Book(id, title, author));
////                    System.out.println("Book Updated");
////                    break;
////
////                case 5:
////                    bookDao.deleteAll();
////                    System.out.println("All Books Deleted");
////                    break;
////
////                case 6:
////                    for (Book b : bookDao.findAll()) {
////                        System.out.println(b);
////                    }
////                    break;
////
////                case 7:
////                    System.out.print("Enter Author: ");
////                    author = sc.nextLine();
////
////                    for (Book b : bookDao.findByAuthor(author)) {
////                        System.out.println(b);
////                    }
////                    break;
////
////                case 8:
////                    System.out.print("Enter Title: ");
////                    title = sc.nextLine();
////
////                    for (Book b : bookDao.findByTitle(title)) {
////                        System.out.println(b);
////                    }
////                    break;
////
////                case 9:
////                    System.out.println("Exiting...");
////                    System.exit(0);
////
////                default:
////                    System.out.println("Invalid Choice");
////            }
////        }
////    }
////}
