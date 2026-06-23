package dao;

import Connection.DBmanager;
import entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    public Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"));
    }

    @Override
    public void save(Product p) throws SQLException {
        Connection connection = DBmanager.getConnection();
        String sql = "insert into product values(?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, p.getId());
        stmt.setString(2, p.getName());
        stmt.setDouble(3, p.getPrice());
        stmt.execute();

        stmt.close();
        connection.close();
    }

    @Override
    public void update(Product p) throws SQLException {
        Connection connection = DBmanager.getConnection();
        String sql = "update product set name =?,price=? where id =?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, p.getName());
        statement.setDouble(2, p.getPrice());
        statement.setInt(3, p.getId());
        int rows = statement.executeUpdate();
        System.out.println("The no.of rows affected are" + rows);
        statement.close();
        connection.close();
    }

    @Override
    public void deletebyid(int id) throws SQLException {
        Connection connection = DBmanager.getConnection();
        String sql = "delete from product where id =?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        int rows = stmt.executeUpdate();
        System.out.println("The no.of rows affected are " + rows);

        stmt.close();
        connection.close();
    }

    @Override
    public void deletebyname(String name) throws SQLException {
        Connection connection = DBmanager.getConnection();
        String sql = "delete from product where name =?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, name);
        int rows = stmt.executeUpdate();
        System.out.println("The rows deleted are " + rows);
        stmt.close();
        connection.close();
    }

    @Override
    public void deleteAll() throws SQLException {
        Connection connection = DBmanager.getConnection();
        String sql = "delete from product";//no thread safety wanted means truncate table product;
        PreparedStatement statement = connection.prepareStatement(sql);
        int rows = statement.executeUpdate();
        System.out.println("The no.of rows deleted are " + rows);
        statement.close();
        connection.close();

    }

    @Override
    public Product findbyid(int id) throws SQLException {
        Connection connection = DBmanager.getConnection();
        String sql = "select * from product where id =?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"));
        }
        return null;

    }

    @Override
    public Product findbyname(String name) throws SQLException {
        Connection connection = DBmanager.getConnection();
        String sql = "select * from product where name =?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"));
        }
        return null;
    }

    @Override
    public List<Product> findall() throws SQLException {

        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";

        try (
                Connection connection = DBmanager.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        }

        return products;
    }
}