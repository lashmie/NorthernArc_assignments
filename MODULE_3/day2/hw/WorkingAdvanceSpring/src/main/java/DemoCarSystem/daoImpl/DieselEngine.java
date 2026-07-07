package DemoCarSystem.daoImpl;

import DemoCarSystem.dao.Engine;
import org.springframework.stereotype.Component;

@Component("diesel")
public class DieselEngine implements Engine {
    @Override
    public void horsepower(int hp) {
        System.out.println("The horse power is"+hp);
    }
}
