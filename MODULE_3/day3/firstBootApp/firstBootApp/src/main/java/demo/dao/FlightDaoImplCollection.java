package demo.dao;

import demo.model.Flight;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class FlightDaoImplCollection implements FlightDao {

    private List<Flight> flights = new ArrayList<>();

    @Override
    public void save(Flight flight) {
        flights.add(flight);
        System.out.println("Flight saved successfully");
    }

    @Override
    public void update(Flight flight) {

        for (int i = 0; i < flights.size(); i++) {

            if (flights.get(i).getFlightNo() == flight.getFlightNo()) {
                flights.set(i, flight);
                System.out.println("Flight updated successfully");
                return;
            }
        }

        System.out.println("Flight not found");
    }

    @Override
    public void deleteByNumber(int number) {

        Flight flight = findByNumber(number);

        if (flight != null) {
            flights.remove(flight);
            System.out.println("Flight deleted successfully");
        } else {
            System.out.println("Flight not found");
        }
    }

    @Override
    public void deleteAll() {
        flights.clear();
        System.out.println("All flights deleted");
    }

    @Override
    public Flight findByNumber(int number) {

        for (Flight flight : flights) {

            if (flight.getFlightNo() == number) {
                return flight;
            }
        }

        return null;
    }

    @Override
    public void findAll() {

        if (flights.isEmpty()) {
            System.out.println("No flights available");
            return;
        }
        for (Flight flight : flights) {
            System.out.println(flight);
        }
    }
}
