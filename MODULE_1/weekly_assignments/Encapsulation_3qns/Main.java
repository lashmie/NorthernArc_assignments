import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter fuel capacity:");
        double cap = sc.nextDouble();

        System.out.println("Enter initial fuel:");
        double fuel = sc.nextDouble();

        FuelTracker ft = new FuelTracker(cap, fuel);

        while (true) {
            System.out.println("\n1.Add Fuel 2.Drive 3.Check Fuel 4.Exit");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("Enter fuel amount:");
                    ft.fuel(sc.nextDouble());
                    break;

                case 2:
                    System.out.println("Enter km:");
                    ft.drive(sc.nextDouble());
                    break;

                case 3:
                    ft.checkFuel();
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}