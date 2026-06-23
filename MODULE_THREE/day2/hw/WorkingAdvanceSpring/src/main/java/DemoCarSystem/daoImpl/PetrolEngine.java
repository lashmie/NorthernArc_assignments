package DemoCarSystem.daoImpl;

import DemoCarSystem.dao.Engine;
import org.springframework.stereotype.Component;

@Component("petrol")
public class PetrolEngine implements Engine {
    @Override
    public void horsepower(int hp) {
        System.out.println("The petrolEngine's horse power is "+hp);
    }
}
