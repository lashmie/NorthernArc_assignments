package BookDemo.dao;

import BookDemo.entity.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class BookDaoImplColl implements BookDao {
    List<Book> bookList = new ArrayList<>();

    @Override
    public void save(Book book) {
        bookList.add(book);
    }

    @Override
    public void update(Book book) {
        for (Book b : bookList) {
            if (b.getId() == book.getId()) {
               b.setId(book.getId());
               b.setName(book.getName());
               b.setPrice(book.getPrice());
            }
        }
    }

    @Override
    public Book findById(int id) {
     for(Book b :bookList){
         if(b.getId()==id){
             return b;
         }
     }
     return null;
    }

    @Override
    public Book findByname(String name) {
        for(Book b :bookList){
            if(b.getName().equalsIgnoreCase(name)){
                return b;
            }
        }
        return null;
    }

    @Override
    public List<Book> findAll() {
        return bookList;
    }

    @Override
    public void deleteAll() {
bookList.clear();
    }

    @Override
    public void deleteById(int id) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getId() == id) {
                bookList.remove(i);
                return;
            }
        }
    }

    @Override
    public void deletebyName(String name) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getName().equalsIgnoreCase(name)) {
                bookList.remove(i);
                return;
            }
        }
    }
}
