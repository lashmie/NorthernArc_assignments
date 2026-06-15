package dao;

import Connection.DBmanager;
import entity.todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class todoImpl implements todoDao {

    @Override
    public void save(todo t) {

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "INSERT INTO todo(id, task, completed) VALUES(?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, t.getId());
            stmt.setString(2, t.getTask());
            stmt.setBoolean(3, t.isCompleted());

            stmt.executeUpdate();

            System.out.println("Todo added successfully");

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    @Override
    public todo findbyId(int id) {

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "SELECT * FROM todo WHERE id=?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                todo t = new todo(
                        rs.getInt("id"),
                        rs.getString("task")
                );

                t.setCompleted(rs.getBoolean("completed"));

                return t;
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return null;
    }

    @Override
    public void update(todo t) {

        try (Connection conn = DBmanager.getConnection()) {

            String sql =
                    "UPDATE todo SET task=?, completed=? WHERE id=?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, t.getTask());
            stmt.setBoolean(2, t.isCompleted());
            stmt.setInt(3, t.getId());

            stmt.executeUpdate();

            System.out.println("Todo updated successfully");

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    @Override
    public void deletebyid(int id) {

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "DELETE FROM todo WHERE id=?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

            stmt.executeUpdate();

            System.out.println("Todo deleted successfully");

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    @Override
    public void deleteall() {

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "DELETE FROM todo";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.executeUpdate();

            System.out.println("All todos deleted");

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    @Override
    public void findall() {

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "SELECT * FROM todo";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                todo t = new todo(
                        rs.getInt("id"),
                        rs.getString("task")
                );

                t.setCompleted(rs.getBoolean("completed"));

                System.out.println(t);
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}