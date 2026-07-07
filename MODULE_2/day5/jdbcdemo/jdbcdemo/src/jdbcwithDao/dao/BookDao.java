package jdbcwithDao.dao;

import jdbcwithDao.entity.Book;

import java.util.Collection;

public interface BookDao {
    public int save(Book book);
    public Book findbyId(int id);
    public void deleteById(int id);
    public void updateByid(int id, Book book);
    public void deleteAll();
    public Collection<Book> findAll();
    public Collection<Book> findByAuthor(String author);
    public Collection<Book> findByTitle(String title);
    public Collection<Book> sortByTitleAsc();     //desc and aesc we have to do ..
    public Collection<Book> sortByTitleDesc();
    public Collection<Book> findByAuthorandPublisher(String author,String publisher);
    public Collection<Book> findByAuthorandtitle(String author,String title);//in between and like
}
