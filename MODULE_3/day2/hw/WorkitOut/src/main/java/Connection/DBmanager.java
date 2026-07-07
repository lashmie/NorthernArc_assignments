package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBmanager {
    private static final String url ="jdbc:postgresql://localhost:5432/mohana";
    private static final String name ="postgres";
    private static final String password="12345";
    public static Connection getConnection() throws SQLException {
        return  DriverManager.getConnection(url,name,password);
    }
    public static void closeConnection(Connection conn){
        if(conn !=null){
            try {
                conn.close();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }

        }
    }
}
