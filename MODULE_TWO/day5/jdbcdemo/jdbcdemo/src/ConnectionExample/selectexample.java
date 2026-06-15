package ConnectionExample;

import java.sql.*;

public class selectexample {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/northernarc";
        String user = "postgres";
        String password = "12345";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Database connected successfully");

            //query
            String sql = "select * from person";
            PreparedStatement smt = conn.prepareStatement(sql);
            System.out.println("Executing the query: ");
            ResultSet res =smt.executeQuery();
            while(res.next()){
                System.out.println(res.getString("name")+" "+res.getString("email"));
                System.out.println(res.getString(1)+" "+res.getString(2));
            }


        } catch (SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
    }
}
