package entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Flight {

    public int flightNo;
    public LocalDate dateOfDeparture;
    public LocalDate dateOfArrival;
    public String source;
    public String destination;
    public LocalTime timeOfArrival;
    public LocalTime timeOfDeparture;
    public Double costPerSeat;
    public int noOfSeats;

    public Flight() {
    }

    public int getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(int flightNo) {
        this.flightNo = flightNo;
    }

    public LocalDate getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(LocalDate dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public LocalDate getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(LocalDate dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalTime getTimeOfArrival() {
        return timeOfArrival;
    }

    public void setTimeOfArrival(LocalTime timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
    }

    public LocalTime getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public void setTimeOfDeparture(LocalTime timeOfDeparture) {
        this.timeOfDeparture = timeOfDeparture;
    }

    public Double getCostPerSeat() {
        return costPerSeat;
    }

    public void setCostPerSeat(Double costPerSeat) {
        this.costPerSeat = costPerSeat;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public Flight(int flightNo, LocalDate dateOfDeparture, LocalDate dateOfArrival,
                  String source, String destination,
                  LocalTime timeOfArrival, LocalTime timeOfDeparture,
                  Double costPerSeat, int noOfSeats) {

        this.flightNo = flightNo;
        this.dateOfDeparture = dateOfDeparture;
        this.dateOfArrival = dateOfArrival;
        this.source = source;
        this.destination = destination;
        this.timeOfArrival = timeOfArrival;
        this.timeOfDeparture = timeOfDeparture;
        this.costPerSeat = costPerSeat;
        this.noOfSeats = noOfSeats;
    }

    public String toString() {
        return "Flight{" +
                "flightNo=" + flightNo +
                ", dateOfDeparture=" + dateOfDeparture +
                ", dateOfArrival=" + dateOfArrival +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", timeOfArrival=" + timeOfArrival +
                ", timeOfDeparture=" + timeOfDeparture +
                ", costPerSeat=" + costPerSeat +
                ", noOfSeats=" + noOfSeats +
                '}';
    }
}