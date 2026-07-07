public class Rectangle implements Shape{
    private double length;
    private double breadth;

    Rectangle(double length,double lengh){
        this.length=length;
        this.breadth=lengh;

    }

    public double getLength(){
        return length;
    }
    public double getBreadth(){
        return breadth;
    }
    public void setLength(double length){
        this.length=length;
    }
    public void setBreadth(double breadth){
        this.breadth=breadth;
    }

    public void calculate(){
        System.out.println("The area is :"+getBreadth()*getBreadth());
    }
}
