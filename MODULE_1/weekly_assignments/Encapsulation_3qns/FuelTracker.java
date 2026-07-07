public class FuelTracker {

    private double fuel_capacity;
    private double current_fuelamount;

    // Constructor
    public FuelTracker(double fuel_capacity, double current_fuelamount) {
        this.fuel_capacity = fuel_capacity;
        this.current_fuelamount = current_fuelamount;
    }

    // topup fuel
    public void fuel(double amount) {
        if (current_fuelamount + amount > fuel_capacity) {
            System.out.println("Tank overflow! Cannot add fuel");
        } else {
            current_fuelamount += amount;
            System.out.println("Fuel added");
        }
    }

    // drive method (40 km per litre)
    public void drive(double km) {
        double requiredFuel = km / 40;

        if (requiredFuel > current_fuelamount) {
            System.out.println("Not enough fuel");
        } else {
            current_fuelamount -= requiredFuel;
            System.out.println("Driven " + km + " km");
        }
    }

    // check fuel
    public void checkFuel() {
        System.out.println("Current fuel: " + current_fuelamount + " litres");
    }
}