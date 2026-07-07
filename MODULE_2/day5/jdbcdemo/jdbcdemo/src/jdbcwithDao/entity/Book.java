package jdbcwithDao.entity;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;

    public Book(int id, String title, String author, String pub) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = pub;

    }
    public Book() {

    }

    public Book(String title, String author, String pub) {

        this.title = title;
        this.author = author;
        this.publisher = pub;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String toString(){
        return "name "+id+" title "+title+" author "+author+" publisher ";
    }
}
