package BookDemo.dao;

import BookDemo.entity.Book;

import java.util.List;

public interface BookDao {
    public void save(Book book);
    public void update(Book book);
    public Book findById(int id);
    public Book findByname(String name);
    public List<Book> findAll();
    public void deleteAll();
    public void deleteById(int id);
    public void deletebyName(String name);
}
