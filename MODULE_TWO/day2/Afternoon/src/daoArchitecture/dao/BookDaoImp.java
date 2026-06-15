package daoArchitecture.dao;

import daoArchitecture.dao.BookDao;
import daoArchitecture.entity.Book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookDaoImp implements BookDao {
    List<Book> books = new ArrayList<>();
    @Override
    public void save(Book book) {
books.add(book);
    }

    @Override
    public Book findbyId(int id) {
        for(Book book:books){
            if(book.getId()==id){
                return book;
            }
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        Iterator<Book> bookIter = books.iterator();

        while (bookIter.hasNext()) {
            Book book = bookIter.next();

            if (book.getId() == id) {
                bookIter.remove();   // removes current book
                break;               // stop after first match
            }
        }
    }

    @Override
    public void update(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == book.getId()) {
                books.set(i, book);
                return;
            }
        }
    }

    @Override
    public void deleteAll() {
books.clear();
    }

    @Override
    public Iterable<Book> findAll() {
        return books;
    }

    @Override
    public Iterable<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }

        return result;
    }

    @Override
    public Iterable<Book> findByTitle(String title) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                result.add(book);
            }
        }

        return result;
    }

//    @Override
//    public Iterable<Book> sortByTitleAsc() {
//        return null;
//    }
//
//    @Override
//    public Iterable<Book> sortByTitleDesc() {
//        return null;
//    }

}
