////package jdbcwithDao.ui;
////
////import jdbcwithDao.dao.BookDao;
////import jdbcwithDao.dao.BookDaoImpl;
////import jdbcwithDao.entity.Book;
////
////import java.util.Scanner;
////
////public class BookApp {
////    private static BookDao bookdao = new BookDaoImpl();
////    private static Scanner sc = new Scanner(System.in);
////
////    public static void main(String[] args) {
////
////    }
////private static void getAll(){
////        bookdao.findAll().forEach(System.out::println);
////}
////    static int addbook() {
////        System.out.println("enter the book,title ,author");
////        Book book = new Book(sc.nextLine(), sc.nextLine(), sc.nextLine());
////        return bookdao.save(book);
////    }
////
////    static Book get() {
////        System.out.println("Enter id: ");
////        int id = sc.nextInt();
////        return bookdao.findbyId(id);
////    }
////
////    static Book get(int id) {
////        return bookdao.findbyId(id);
////    }
////}
package jdbcwithDao.ui;

import jdbcwithDao.dao.BookDao;
import jdbcwithDao.dao.BookDaoImpl;
import jdbcwithDao.entity.Book;

import java.util.Scanner;

public class BookApp {

    private static BookDao bookdao = new BookDaoImpl();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n===== BOOK MENU =====");
            System.out.println("1. Add Book");
            System.out.println("2. Get Book by ID");
            System.out.println("3. Get All Books");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    int rows = addbook();
                    System.out.println(rows + " book inserted.");
                    break;

                case 2:
                    Book book = get();
                    if (book != null) {
                        System.out.println(book);
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case 3:
                    getAll();
                    break;

                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void getAll() {
        bookdao.findAll().forEach(System.out::println);
    }

    static int addbook() {
        System.out.println("Enter title, author, publisher:");

        String title = sc.nextLine();
        String author = sc.nextLine();
        String publisher = sc.nextLine();

        Book book = new Book(title, author, publisher);
        return bookdao.save(book);
    }

    static Book get() {
        System.out.println("Enter id: ");
        int id = sc.nextInt();
        sc.nextLine();
        return bookdao.findbyId(id);
    }

    static Book get(int id) {
        return bookdao.findbyId(id);
    }
}



