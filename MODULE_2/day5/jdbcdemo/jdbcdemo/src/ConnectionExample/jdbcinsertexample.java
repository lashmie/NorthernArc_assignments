package ConnectionExample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class jdbcinsertexample {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/northernarc";
        String user = "postgres";
        String password = "12345";

        try(Connection conn = DriverManager.getConnection(url,user,password)){
            System.out.println("Database connected successfully");

            //query
            String sql = "UPDATE person SET age = 20 WHERE name = 'lavanya'";

            PreparedStatement smt = conn.prepareStatement(sql);

            System.out.println("Executing the query: " + sql);

            int rowsAffected = smt.executeUpdate();t

            System.out.println(rowsAffected + " row(s) updated");

        }
        catch (SQLException e){
            System.out.println("Failed");
            e.printStackTrace();
        }
    }
}
