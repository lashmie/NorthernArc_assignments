package composition;

public class Car {
    private String name;
    private Engine engine;
    Car(String name,Engine engine){
        this.name=name;
        this.engine= new Engine(90);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
    public void start(){
        System.out.println("Car is starting");
        engine.startEngine();
    }


}
