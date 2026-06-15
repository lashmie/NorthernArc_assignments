package jdbcwithDao.dao;

import Connection.DBmanager;
import jdbcwithDao.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookDaoImpl implements BookDao {
    @Override
    public int save(Book book) {
        try {
            Connection conn = DBmanager.getConnection();
            String sql = "INSERT INTO book(title,author,publisher) values(?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            int rows = stmt.executeUpdate();
            DBmanager.closeConnection(conn);
            return rows;

        } catch (SQLException e) {
            System.out.println("Error in connecting to database" + e.getMessage());
        }

        return 0;
    }

    @Override
    public Book findbyId(int id) {
        try {
            Connection conn = DBmanager.getConnection();
            String sql = "select * from book where id =?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToBook(rs);
            }

        } catch (SQLException e) {
            System.out.println("Issue in connectivity" + e.getMessage());
        }
        return null;
    }
    @Override
    public void deleteById(int id) {
        try (Connection conn = DBmanager.getConnection()) {
            String sql = "DELETE FROM book WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Issue in connectivity: " + e.getMessage());
        }
    }

    @Override
    public void updateByid(int id, Book book) {
        try (Connection conn = DBmanager.getConnection()) {
            String sql = "UPDATE book SET title=?, author=?, publisher=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setInt(4, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Issue in connectivity: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        try (Connection conn = DBmanager.getConnection()) {
            String sql = "DELETE FROM book";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Issue in connectivity: " + e.getMessage());
        }
    }

    @Override
    public Collection<Book> findAll() {
        ArrayList<Book> books = new ArrayList<>();

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "SELECT * FROM book";
            PreparedStatement statement = conn.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

        return books;
    }

    @Override
    public Collection<Book> findByAuthor(String author) {
        ArrayList<Book> books = new ArrayList<>();

        try (Connection conn = DBmanager.getConnection()) {
            String sql = "SELECT * FROM book WHERE author = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, author);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

        } catch (SQLException e) {
            System.out.println("Issue in connectivity: " + e.getMessage());
        }

        return books;
    }

    @Override
    public Collection<Book> findByTitle(String title) {
        ArrayList<Book> books = new ArrayList<>();

        try (Connection conn = DBmanager.getConnection()) {
            String sql = "SELECT * FROM book WHERE title = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

        } catch (SQLException e) {
            System.out.println("Issue in connectivity: " + e.getMessage());
        }

        return books;
    }

    @Override
    public Collection<Book> sortByTitleAsc() {
        ArrayList<Book> books = new ArrayList<>();

        try (Connection conn = DBmanager.getConnection()) {
            String sql = "SELECT * FROM book ORDER BY title ASC";
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

        } catch (SQLException e) {
            System.out.println("Issue in connectivity: " + e.getMessage());
        }

        return books;
    }

    @Override
    public Collection<Book> sortByTitleDesc() {
        ArrayList<Book> books = new ArrayList<>();

        try (Connection conn = DBmanager.getConnection()) {
            String sql = "SELECT * FROM book ORDER BY title DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

        } catch (SQLException e) {
            System.out.println("Issue in connectivity: " + e.getMessage());
        }

        return books;
    }

    @Override
    public Collection<Book> findByAuthorandPublisher(String author, String publisher) {
        ArrayList<Book> books = new ArrayList<>();

        try (Connection conn = DBmanager.getConnection()) {
            String sql = "SELECT * FROM book WHERE author=? AND publisher=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, author);
            stmt.setString(2, publisher);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

        } catch (SQLException e) {
            System.out.println("Issue in connectivity: " + e.getMessage());
        }

        return books;
    }
    @Override
    public Collection<Book> findByAuthorandtitle(String author, String title) {
        ArrayList<Book> books = new ArrayList<>();

        try (Connection conn = DBmanager.getConnection()) {
            String sql = "SELECT * FROM book WHERE author=? AND title=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, author);
            stmt.setString(2, title);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

        } catch (SQLException e) {
            System.out.println("Issue in connectivity: " + e.getMessage());
        }

        return books;
    }

//    @Override
//    public Book findbyId(int id) {
//        try (Connection conn = DBmanager.getConnection()) {
//            String sql = "SELECT * FROM book WHERE id=?";
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, id);
//
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return mapToBook(rs);
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Issue in connectivity: " + e.getMessage());
//        }
//
//        return null;
//    }

    public Book mapToBook(ResultSet rs) throws SQLException {
        return new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("publisher"));
    }
}
