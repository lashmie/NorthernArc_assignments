public class Main4 {
    public static void main(String[] args) {
        Book b = new Book();
        b.setTitle("ramayanam");
        b.setAuthor("valmiki");
        b.setPages(1000);
        System.out.println("The number of pages is "+b.getPages());
        System.out.println("The author of the book "+b.getTitle()+"is "+b.getAuthor());
    }
}
