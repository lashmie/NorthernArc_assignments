package aggregation.classassigment;

public class Player {
    private String pname;
    private double rating;

    Player(String pname,double rating){
        this.pname=pname;
        this.rating=rating;
    }

    public String getPname(){
        return pname;
    }

    public double getRating(){
        return getRating();
    }

    public String toString() {
        return "Playername"+pname+" rating"+rating;
    }

}
