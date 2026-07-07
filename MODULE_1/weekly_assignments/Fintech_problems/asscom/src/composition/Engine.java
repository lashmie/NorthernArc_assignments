package composition;

public class Engine {
    private int horsepower;
    Engine(int horsepower){
        this.horsepower=horsepower;
    }
    public void startEngine(){
        System.out.println("Engine is starting");
    }
}
