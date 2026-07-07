public class Movie{
    private String title;
    private String director;
    private int releaseyear;

    //main5.java 
    public Movie(String title,String director,int year){
        this.title=title;
        this.director=director;
        this.releaseyear=year;
    }

    public void setTitle(String title){
        this.title=title;
    }
     public void setDirector(String director ){
        this.director=director;
    }
     public void setreleaseyear(int year ){
        this.releaseyear=year;
    }

     public String getTitle(){
        return title;
    }
     public String getDirector(){
        return director;
    }

    public int getReleaseyear(){
        return releaseyear;
    }


    public void play(){
        System.out.println("The movie "+ title +"is amazing work by "+director);
    }
    public static void getRating(){
        System.out.println("The rating was good ");

    }


}





