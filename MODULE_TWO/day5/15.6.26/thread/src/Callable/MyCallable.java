package Callable;

import java.util.concurrent.Callable;

public class MyCallable implements Callable {
    @Override
    public Integer call() throws Exception {
        Thread.sleep((int)(10000*Math.random()));
        System.out.println("Callable is called by "+Thread.currentThread().getName());
        return (int)(Math.random()*10000);
    }
}
