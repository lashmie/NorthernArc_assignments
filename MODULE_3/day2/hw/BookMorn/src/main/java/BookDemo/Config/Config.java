package BookDemo.Config;

import BookDemo.Connection.DBManager;
import BookDemo.Controller.Controller;
import BookDemo.dao.BookDao;
import BookDemo.dao.BookDaoImplColl;
import BookDemo.dao.BookDaoImplJdbc;
import BookDemo.entity.Book;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class Config {
    @Bean
    public DBManager dbManager(){
        return new DBManager();
    }
    @Bean
    public Scanner scanner(){
        return new Scanner(System.in);
    }
    @Bean("coll")
    public BookDao book(){
        return new BookDaoImplColl();
    }
    @Bean("jdbc")
    public BookDao bookJdbc(DBManager dbManager){
        return new BookDaoImplJdbc(dbManager);
    }
    @Bean
    public Controller cc(Scanner scanner,BookDao book){
        return new Controller(scanner,book);
    }
}
