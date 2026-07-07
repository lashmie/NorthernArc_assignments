package Config;


import dao.DaoTodo;
import dao.DaoTodoImplColl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.BeanProperty;
@Configuration
public class SpringConfiguration {
    @Bean
    public DaoTodo todoDao(){
        return new DaoTodoImplColl();
    }
}
