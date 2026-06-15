package daoArchitecture.entity;

public class Book {
    private int id;
    private String title;
    private String author;
    public Book(int id, String title, String author){
         this.id=id;
         this.title=title;
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

//    public static void Pintdaata(){
//        System.out.println("kabikanna");
//    }
}
