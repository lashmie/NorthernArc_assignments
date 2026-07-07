package DemoCarSystem.daoImpl;

import DemoCarSystem.dao.Music;
import org.springframework.stereotype.Component;

@Component("sony")
public class SonyMusic implements Music {
    @Override
    public void play() {
        System.out.println("Sony music is playing...................");
    }
}
