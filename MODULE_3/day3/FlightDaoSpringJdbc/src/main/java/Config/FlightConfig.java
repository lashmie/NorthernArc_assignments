package Config;

import Connection.DBManager;
import controller.ConsoleController;
import dao.FlightDao;
import dao.FlightDaoImplCollection;
import dao.FlightDaoImplJdbc;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class FlightConfig {
    @Bean
    public DBManager dbManager() {
        return new DBManager();
    }
    @Bean
    public Scanner scanner(){
        return new Scanner(System.in);
    }
    @Bean("fdc")
    public FlightDao flightDao(){
        return new FlightDaoImplCollection();
    }
    @Bean("fdj")
    public FlightDao flightDaoJd(DBManager dbManager){
        return new FlightDaoImplJdbc(dbManager);
    }
    @Bean("ccc")
    public ConsoleController cc(Scanner s,@Qualifier("fdc") FlightDao f){
        return new ConsoleController(s,f);
    }
    @Bean("ccj")
    public ConsoleController cj(Scanner s,@Qualifier("fdj")FlightDao f){
        return new ConsoleController(s,f);
    }


}
