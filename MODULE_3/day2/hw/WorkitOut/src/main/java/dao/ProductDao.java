package dao;

import entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    public void save(Product p)throws SQLException;
    public void update(Product p)throws SQLException;
    public void deletebyid(int id)throws SQLException;
    public void deletebyname(String name)throws SQLException;
    public  void deleteAll()throws SQLException;
    public Product findbyid(int id)throws SQLException;
    public  Product findbyname(String name)throws SQLException;
    public List<Product> findall()throws SQLException;
}
