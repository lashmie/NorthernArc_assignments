package dao;
import Connection.DBmanager;
import entity.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDaoImpl implements PersonDao {

    @Override
    public void save(Person person) {

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "INSERT INTO person(name, age, education) VALUES(?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, person.getName());
            stmt.setInt(2, person.getAge());
            stmt.setString(3, person.getEducation());

            stmt.executeUpdate();

            System.out.println("Person saved successfully");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public List<Person> findAll() {

        List<Person> persons = new ArrayList<>();

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "SELECT * FROM person";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Person person = new Person(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("education")
                );

                persons.add(person);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return persons;
    }

    @Override
    public void update(Person person) {

        try (Connection conn = DBmanager.getConnection()) {

            String sql =
                    "UPDATE person SET age=?, education=? WHERE name=?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, person.getAge());
            stmt.setString(2, person.getEducation());
            stmt.setString(3, person.getName());

            stmt.executeUpdate();

            System.out.println("Person updated successfully");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(String name) {

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "DELETE FROM person WHERE name=?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, name);

            stmt.executeUpdate();

            System.out.println("Person deleted successfully");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "DELETE FROM person";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.executeUpdate();

            System.out.println("All persons deleted");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}