package demo.controller;



import demo.dao.FlightDao;
import demo.model.Flight;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
@Component
public class FlightController {

    private Scanner scanner;
    private FlightDao flightDao;

    public FlightController(Scanner scanner, FlightDao flightDao) {
        this.scanner = scanner;
        this.flightDao = flightDao;
    }

    public void showMenu() {

        while (true) {

            System.out.println("\n===== FLIGHT MENU =====");
            System.out.println("1. Save");
            System.out.println("2. Update");
            System.out.println("3. Delete By Number");
            System.out.println("4. Delete All");
            System.out.println("5. Find By Number");
            System.out.println("6. Find All");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> save();
                case 2 -> update();
                case 3 -> deleteByNumber();
                case 4 -> deleteAll();
                case 5 -> findByNumber();
                case 6 -> findAll();
                case 7 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid Choice");
            }
        }
    }

    private void save() {

        Flight flight = new Flight();

        System.out.print("Flight Number: ");
        flight.setFlightNo(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Date Of Departure (yyyy-mm-dd): ");
        flight.setDateOfDeparture(LocalDate.parse(scanner.nextLine()));

        System.out.print("Date Of Arrival (yyyy-mm-dd): ");
        flight.setDateOfArrival(LocalDate.parse(scanner.nextLine()));

        System.out.print("Source: ");
        flight.setSource(scanner.nextLine());

        System.out.print("Destination: ");
        flight.setDestination(scanner.nextLine());

        System.out.print("Time Of Arrival (HH:mm:ss): ");
        flight.setTimeOfArrival(LocalTime.parse(scanner.nextLine()));

        System.out.print("Time Of Departure (HH:mm:ss): ");
        flight.setTimeOfDeparture(LocalTime.parse(scanner.nextLine()));

        System.out.print("Cost Per Seat: ");
        flight.setCostPerSeat(scanner.nextDouble());

        System.out.print("Number Of Seats: ");
        flight.setNoOfSeats(scanner.nextInt());

        flightDao.save(flight);

        System.out.println("Flight Saved Successfully");
    }

    private void update() {

        Flight flight = new Flight();

        System.out.print("Flight Number to Update: ");
        flight.setFlightNo(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Date Of Departure (yyyy-mm-dd): ");
        flight.setDateOfDeparture(LocalDate.parse(scanner.nextLine()));

        System.out.print("Date Of Arrival (yyyy-mm-dd): ");
        flight.setDateOfArrival(LocalDate.parse(scanner.nextLine()));

        System.out.print("Source: ");
        flight.setSource(scanner.nextLine());

        System.out.print("Destination: ");
        flight.setDestination(scanner.nextLine());

        System.out.print("Time Of Arrival (HH:mm:ss): ");
        flight.setTimeOfArrival(LocalTime.parse(scanner.nextLine()));

        System.out.print("Time Of Departure (HH:mm:ss): ");
        flight.setTimeOfDeparture(LocalTime.parse(scanner.nextLine()));

        System.out.print("Cost Per Seat: ");
        flight.setCostPerSeat(scanner.nextDouble());

        System.out.print("Number Of Seats: ");
        flight.setNoOfSeats(scanner.nextInt());

        flightDao.update(flight);
    }

    private void deleteByNumber() {

        System.out.print("Enter Flight Number: ");
        int number = scanner.nextInt();

        flightDao.deleteByNumber(number);
    }

    private void deleteAll() {

        System.out.print("Are you sure? (y/n): ");
        String choice = scanner.next();

        if (choice.equalsIgnoreCase("y")) {
            flightDao.deleteAll();
        }
    }

    private void findByNumber() {

        System.out.print("Enter Flight Number: ");
        int number = scanner.nextInt();

        Flight flight = flightDao.findByNumber(number);

        if (flight == null) {
            System.out.println("Flight Not Found");
            return;
        }

        System.out.println("\nFlight Details");
        System.out.println("Flight No      : " + flight.getFlightNo());
        System.out.println("Departure Date : " + flight.getDateOfDeparture());
        System.out.println("Arrival Date   : " + flight.getDateOfArrival());
        System.out.println("Source         : " + flight.getSource());
        System.out.println("Destination    : " + flight.getDestination());
        System.out.println("Arrival Time   : " + flight.getTimeOfArrival());
        System.out.println("Departure Time : " + flight.getTimeOfDeparture());
        System.out.println("Cost Per Seat  : " + flight.getCostPerSeat());
        System.out.println("No Of Seats    : " + flight.getNoOfSeats());
    }

    private void findAll() {
        flightDao.findAll();
    }
}
