public class Book {
    private String title;
    private String author;
    private int pages;
    //Main4 
    public void setTitle(String title){
        this.title=title;
    }

    public void setAuthor(String author){
        this.author=author;
    }

    public void setPages(int pages){
        this.pages=pages;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public int getPages(){
        return pages;
    }

    public void read(){
        System.out.println(" we can get good amount of knowledge");
    }
    public void getSummary(){
            System.out.println("title"+title);
            System.out.println("author"+author);
            System.out.println("pages"+pages);

    }
}
