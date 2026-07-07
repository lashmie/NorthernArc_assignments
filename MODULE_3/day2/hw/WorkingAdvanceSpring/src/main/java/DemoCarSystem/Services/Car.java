package DemoCarSystem.Services;

import DemoCarSystem.dao.Engine;
import DemoCarSystem.dao.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Car {
    @Autowired
    @Qualifier("petrol")
    private Engine engine;
    @Autowired
    @Qualifier("sony")
    private Music music;
//    Car(Engine e,Music m){
//        this.engine=e;
//        this.music=m;
//    }
    public void drive(){
        System.out.println("car is started");
        engine.horsepower(120);
        music.play();
    }
}
