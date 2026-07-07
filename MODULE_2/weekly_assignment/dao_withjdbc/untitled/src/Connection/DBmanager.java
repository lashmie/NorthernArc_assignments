package Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBmanager {
    public static final String db_url="jdbc:postgresql://localhost:5432/northernarc";
    public static final String user = "postgres";
    public static final String password = "12345";//we should not change the password to be changed so we have used final

    public  static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(db_url,user,password);
    }
    public static void closeConnection(Connection connection){
        if(connection != null){
            try{
                connection.close();
            }
            catch (SQLException e){
                System.out.println("Error closing connection: "+e.getMessage());
            }
        }
    }

}
