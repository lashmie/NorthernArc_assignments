//package dao;
//
//import Connection.DBmanager;
//import entity.todo;
//
//import java.sql.*;
//import java.util.*;
//
//public class DaoTodoImplColl implements DaoTodo {
//
//    @Override
//    public void save(todo t) {
//        String sql = "INSERT INTO todo (id, content, done) VALUES (?, ?, ?)";
//
//        try (Connection con = DBmanager.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setInt(1, t.getId());
//            ps.setString(2, t.getTask());
//            ps.setBoolean(3, t.getCompleted());
//
//            ps.executeUpdate();
//
//        } catch (SQLException e) {
//            System.out.println("Save error: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public todo findbyId(int id) {
//        String sql = "SELECT * FROM todo WHERE id = ?";
//        todo t = null;
//
//        try (Connection con = DBmanager.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                t = new todo(
//                        rs.getInt("id"),
//                        rs.getString("content"),
//                        rs.getBoolean("done")
//                );
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Find error: " + e.getMessage());
//        }
//
//        return t;
//    }
//
//    @Override
//    public void update(todo t) {
//        String sql = "UPDATE todo SET content = ?, done = ? WHERE id = ?";
//
//        try (Connection con = DBmanager.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, t.getTask());
//            ps.setBoolean(2, t.getCompleted());
//            ps.setInt(3, t.getId());
//
//            ps.executeUpdate();
//
//        } catch (SQLException e) {
//            System.out.println("Update error: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void deletebyid(int id) {
//        String sql = "DELETE FROM todo WHERE id = ?";
//
//        try (Connection con = DBmanager.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setInt(1, id);
//            ps.executeUpdate();
//
//        } catch (SQLException e) {
//            System.out.println("Delete error: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void deleteall() {
//        String sql = "DELETE FROM todo";
//
//        try (Connection con = DBmanager.getConnection();
//             Statement st = con.createStatement()) {
//
//            st.executeUpdate(sql);
//
//        } catch (SQLException e) {
//            System.out.println("Delete all error: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public Map<Integer, todo> findall() {
//        String sql = "SELECT * FROM todo";
//        Map<Integer, todo> map = new LinkedHashMap<>();
//
//        try (Connection con = DBmanager.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            while (rs.next()) {
//                todo t = new todo(
//                        rs.getInt("id"),
//                        rs.getString("content"),
//                        rs.getBoolean("done")
//                );
//
//                map.put(t.getId(), t);
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Find all error: " + e.getMessage());
//        }
//
//        return map;
//    }
//}
package dao;

import entity.todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoTodoImplJdbc implements DaoTodo {

    private final String url = "jdbc:postgresql://localhost:5432/your_db";
    private final String user = "postgres";
    private final String password = "your_password";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void save(todo t) {
        String sql = "INSERT INTO todo (id, task, completed) VALUES (?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, t.getId());
            ps.setString(2, t.getTask());
            ps.setBoolean(3, t.getCompleted());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public todo findbyId(int id) {
        String sql = "SELECT * FROM todo WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new todo(
                        rs.getInt("id"),
                        rs.getString("task"),
                        rs.getBoolean("completed")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<todo> findall() {
        List<todo> list = new ArrayList<>();
        String sql = "SELECT * FROM todo";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new todo(
                        rs.getInt("id"),
                        rs.getString("task"),
                        rs.getBoolean("completed")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void update(todo t) {
        String sql = "UPDATE todo SET task = ?, completed = ? WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getTask());
            ps.setBoolean(2, t.getCompleted());
            ps.setInt(3, t.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletebyid(int id) {
        String sql = "DELETE FROM todo WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteall() {

    }
}