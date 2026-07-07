package BookDemo.dao;

import BookDemo.entity.Book;
import BookDemo.Connection.DBManager;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImplJdbc implements BookDao {

    private final DBManager dbManager;

    public BookDaoImplJdbc(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // SAVE
    @Override
    public void save(Book book) {
        Connection con = dbManager.getConnection();

        String sql = "INSERT INTO book(id, name, price) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, book.getId());
            ps.setString(2, book.getName());
            ps.setDouble(3, book.getPrice());

            ps.executeUpdate();

            System.out.println("Book saved successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection(con);
        }
    }

    // UPDATE
    @Override
    public void update(Book book) {
        Connection con = dbManager.getConnection();

        String sql = "UPDATE book SET name=?, price=? WHERE id=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, book.getName());
            ps.setDouble(2, book.getPrice());
            ps.setInt(3, book.getId());

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Book updated successfully");
            else
                System.out.println("Book not found");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection(con);
        }
    }

    // FIND BY ID
    @Override
    public Book findById(int id) {
        Connection con = dbManager.getConnection();

        String sql = "SELECT * FROM book WHERE id=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Book(

                        rs.getString("name"),
                        rs.getInt("id"),
                        rs.getDouble("price")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection(con);
        }

        return null;
    }

    // FIND BY NAME
    @Override
    public Book findByname(String name) {
        Connection con = dbManager.getConnection();

        String sql = "SELECT * FROM book WHERE name=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Book(

                        rs.getString("name"),
                        rs.getInt("id"),
                        rs.getDouble("price")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection(con);
        }

        return null;
    }

    // FIND ALL
    @Override
    public List<Book> findAll() {
        Connection con = dbManager.getConnection();

        List<Book> list = new ArrayList<>();

        String sql = "SELECT * FROM book";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Book(

                        rs.getString("name"),
                        rs.getInt("id"),
                        rs.getDouble("price")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection(con);
        }

        return list;
    }

    // DELETE ALL
    @Override
    public void deleteAll() {
        Connection con = dbManager.getConnection();

        String sql = "DELETE FROM book";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();

            System.out.println("All books deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection(con);
        }
    }

    // DELETE BY ID
    @Override
    public void deleteById(int id) {
        Connection con = dbManager.getConnection();

        String sql = "DELETE FROM book WHERE id=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Book deleted");
            else
                System.out.println("Book not found");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection(con);
        }
    }

    // DELETE BY NAME
    @Override
    public void deletebyName(String name) {
        Connection con = dbManager.getConnection();

        String sql = "DELETE FROM book WHERE name=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Book deleted");
            else
                System.out.println("Book not found");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection(con);
        }
    }
}