package dao;

import Connection.DBManager;
import entity.Flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

public class FlightDaoImplJdbc implements FlightDao {
    private DBManager dbManager;

    public FlightDaoImplJdbc(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void save(Flight flight) {
        Connection conn = dbManager.getConnection();
        String sql = "insert into flight values(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, flight.getFlightNo());
            stmt.setDate(2, java.sql.Date.valueOf(flight.getDateOfDeparture()));
            stmt.setDate(3, java.sql.Date.valueOf(flight.getDateOfArrival()));
            stmt.setString(4, flight.getSource());
            stmt.setString(5, flight.getDestination());
            stmt.setTime(6, java.sql.Time.valueOf(flight.getTimeOfArrival()));
            stmt.setTime(7, java.sql.Time.valueOf(flight.getTimeOfDeparture()));
            stmt.setDouble(8, flight.getCostPerSeat());
            stmt.setInt(9, flight.getNoOfSeats());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error :" + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }

    }

    @Override
    public void update(Flight flight) {
        Connection conn = dbManager.getConnection();
        String sql =
                "UPDATE flight SET dateOfDeparture=?, dateOfArrival=?, source=?, destination=?, " +
                        "timeOfArrival=?, timeOfDeparture=?, costPerSeat=?, noOfSeats=? WHERE flightNo=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setObject(1, flight.dateOfDeparture);
            stmt.setObject(2, flight.dateOfArrival);
            stmt.setString(3, flight.source);
            stmt.setString(4, flight.destination);
            stmt.setObject(5, flight.timeOfArrival);
            stmt.setObject(6, flight.timeOfDeparture);
            stmt.setDouble(7, flight.costPerSeat);
            stmt.setInt(8, flight.noOfSeats);

            // WHERE condition (IMPORTANT)
            stmt.setInt(9, flight.flightNo);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Flight updated successfully");
            } else {
                System.out.println("Flight not found");
            }
        } catch (SQLException e) {
            System.out.println("Database error :" + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }

    }
    @Override
    public void deleteAll() {
        Connection conn = dbManager.getConnection();

        String sql = "DELETE FROM flight";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            int rows = stmt.executeUpdate();

            System.out.println(rows + " rows deleted");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }
    }
    @Override
    public Flight findByNumber(int number) {

        Connection conn = dbManager.getConnection();

        String sql = "SELECT * FROM flight WHERE flightno = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, number);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Flight flight = new Flight();

                flight.setFlightNo(rs.getInt("flightno"));
                flight.setDateOfDeparture(rs.getDate("date_of_departure").toLocalDate());
                flight.setDateOfArrival(rs.getDate("date_of_arrival").toLocalDate());
                flight.setSource(rs.getString("source"));
                flight.setDestination(rs.getString("destination"));
                flight.setTimeOfArrival(rs.getTime("time_of_arrival").toLocalTime());
                flight.setTimeOfDeparture(rs.getTime("time_of_departure").toLocalTime());
                flight.setCostPerSeat(rs.getDouble("cost_perseat"));
                flight.setNoOfSeats(rs.getInt("no_of_seats"));

                return flight;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }

        return null;
    }
    @Override
    public void findAll() {

        Connection conn = dbManager.getConnection();

        String sql = "SELECT * FROM flight";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                System.out.println(
                        rs.getInt("flightno") + " "
                                + rs.getString("source") + " -> "
                                + rs.getString("destination"));
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }
    }
    @Override
    public void deleteByNumber(int number) {
        Connection conn = dbManager.getConnection();

        String sql = "DELETE FROM flight WHERE flightno = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, number);

            int val = stmt.executeUpdate();

            if (val > 0) {
                System.out.println("The row is deleted");
            } else {
                System.out.println("No flight found with number " + number);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }
    }
}
