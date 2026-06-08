public class Car {
    private String make;
    private String model;
    private String year;
    public Car(String make,String model,String year){
        this.make=make;
        this.model=model;
        this.year=year;

    }
    public void start(){
        System.out.println("The car is starting");
    }
    public void stop(){
        System.out.println("The model"+model
        +"is stopping");
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


    public void getDetails(){
        System.out.println("Make"+this.make+"\n"+"model"+this.model+"\n"+"year"+year+"\n");
    }
}
