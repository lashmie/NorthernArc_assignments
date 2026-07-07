package DemoCarSystem.daoImpl;

import DemoCarSystem.dao.Music;
import org.northernarc.Main;
import org.springframework.stereotype.Component;

@Component("bose")
public class BoseMusic implements Music {
    @Override
    public void play() {
        System.out.println("Bose music is playing.......");
    }
}
